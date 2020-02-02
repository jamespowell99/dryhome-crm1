package uk.co.dryhome.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.dryhome.domain.CustomerOrder;
import uk.co.dryhome.domain.OrderItem;
import uk.co.dryhome.domain.enumeration.OrderStatus;
import uk.co.dryhome.repository.CustomerOrderRepository;
import uk.co.dryhome.repository.OrderItemRepository;
import uk.co.dryhome.repository.ProductRepository;
import uk.co.dryhome.service.dto.CustomerOrderDetailDTO;
import uk.co.dryhome.service.dto.CustomerOrderSummaryDTO;
import uk.co.dryhome.service.dto.OrderItemDTO;
import uk.co.dryhome.service.mapper.CustomerOrderMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing CustomerOrder.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerOrderService {

    private final Logger log = LoggerFactory.getLogger(CustomerOrderService.class);

    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerOrderMapper customerOrderMapper;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;


    public CustomerOrderDetailDTO create(CustomerOrderDetailDTO customerOrderDetailDTO) {
        log.debug("Request to create CustomerOrder : {}", customerOrderDetailDTO);
        final CustomerOrder customerOrder = customerOrderMapper.detailToEntity(customerOrderDetailDTO);

        customerOrder.getItems().forEach(i -> {
            i.setSubTotal(i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())));
        });
        customerOrder.setSubTotal(customerOrder.getItems().stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        customerOrder.setVatAmount(customerOrder.getSubTotal().multiply(customerOrder.getVatRate().divide(BigDecimal.valueOf(100),3, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP));
        customerOrder.setTotal(customerOrder.getSubTotal().add(customerOrder.getVatAmount()));

        customerOrder.setStatus(getStatus(customerOrder));

        CustomerOrder savedCustomerOrder = customerOrderRepository.save(customerOrder);
        customerOrder.getItems().forEach( i -> {
            i.setCustomerOrder(savedCustomerOrder);
            i.setProduct(productRepository.findById(i.getProduct().getId()).orElseThrow(() -> new RuntimeException("product not found: "+ i.getProduct().getId())));
            orderItemRepository.save(i);
        });
        return customerOrderMapper.toDetailDto(savedCustomerOrder);

    }

    private OrderStatus getStatus(CustomerOrder customerOrder) {
        if (customerOrder.getPaymentDate() != null) {
            return OrderStatus.PAID;
        } else if (customerOrder.getInvoiceDate() != null) {
            return OrderStatus.INVOICED;
        } else if (customerOrder.getDespatchDate() != null) {
            return OrderStatus.DESPATCHED;
        } else {
            return OrderStatus.PLACED;
        }
    }

    /**
     * Save a customerOrder.
     *
     * @param customerOrderDetailDTO the entity to save
     * @return the persisted entity
     */
    public CustomerOrderDetailDTO save(CustomerOrderDetailDTO customerOrderDetailDTO) {
        log.debug("Request to save CustomerOrder : {}", customerOrderDetailDTO);

        CustomerOrder existingOrder = customerOrderRepository.findById(customerOrderDetailDTO.getId()).orElseThrow(() -> new RuntimeException("order not found: " + customerOrderDetailDTO.getId()));
        Set<OrderItem> oldItems = existingOrder.getItems();

        final CustomerOrder newCustomerOrder = customerOrderMapper.detailToEntity(customerOrderDetailDTO);
        Set<OrderItem> newItems = newCustomerOrder.getItems();
        newItems.removeIf( x-> x.getId() == null);

        //Update existing items
        newItems.forEach( i ->{
            OrderItem existingItem = oldItems.stream()
                .filter(oi -> oi.getId().equals(i.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("attempting to edit unknown item: " + i.getId()));
            //existing item
            existingItem.setProduct(productRepository.findById(i.getProduct().getId()).orElseThrow(() -> new RuntimeException("Product not found: " + i.getProduct().getId())));
            existingItem.setPrice(i.getPrice());
            existingItem.setQuantity(i.getQuantity());
            existingItem.setNotes(i.getNotes());
            existingItem.setSerialNumber(i.getSerialNumber());
            existingItem.setSubTotal(existingItem.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));

            log.info("Updating item: {}", existingItem);
            orderItemRepository.save(existingItem);
        });

        //add new items
        customerOrderDetailDTO.getItems().stream()
            .filter(i -> i.getId() == null)
            .map(customerOrderMapper::itemToEntity)
            .map(ie -> ie.customerOrder(newCustomerOrder))
            .forEach(ie -> {
                ie.setProduct(productRepository.findById(ie.getProduct().getId()).orElseThrow(() -> new RuntimeException("product not found: " + ie.getProduct().getId())));
                ie.setSubTotal(ie.getPrice().multiply(BigDecimal.valueOf(ie.getQuantity())));

                orderItemRepository.save(ie);
                newCustomerOrder.addItems(ie);
            });

        //delete old items
        List<OrderItem> itemsToDelete = oldItems.stream()
            .filter(x -> !customerOrderDetailDTO.getItems().stream()
                .map(OrderItemDTO::getId)
                .collect(Collectors.toList())
                .contains(x.getId()))
            .collect(Collectors.toList());
        log.info("Deleting items: {}", itemsToDelete);
        orderItemRepository.deleteAll(itemsToDelete);

        CustomerOrder savedCustomerOrder = customerOrderRepository.save(newCustomerOrder);

        savedCustomerOrder.setSubTotal(savedCustomerOrder.getItems().stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        savedCustomerOrder.setVatAmount(savedCustomerOrder.getSubTotal().multiply(savedCustomerOrder.getVatRate().divide(BigDecimal.valueOf(100),3, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP));
        savedCustomerOrder.setTotal(savedCustomerOrder.getSubTotal().add(savedCustomerOrder.getVatAmount()));

        savedCustomerOrder.setStatus(getStatus(savedCustomerOrder));

        CustomerOrderDetailDTO result = customerOrderMapper.toDetailDto(savedCustomerOrder);
        return result;
    }

    /**
     * Get all the customerOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CustomerOrderSummaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerOrders");
        return customerOrderRepository.findAll(pageable)
            .map(customerOrderMapper::toDto);
    }


    /**
     * Delete the customerOrder by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerOrder : {}", id);
        orderItemRepository.deleteByCustomerOrderId(id);
        customerOrderRepository.deleteById(id);
    }

}
