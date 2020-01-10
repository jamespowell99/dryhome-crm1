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
import uk.co.dryhome.repository.CustomerOrderRepository;
import uk.co.dryhome.repository.OrderItemRepository;
import uk.co.dryhome.repository.ProductRepository;
import uk.co.dryhome.service.dto.CustomerOrderDTO;
import uk.co.dryhome.service.dto.CustomerOrderDetailDTO;
import uk.co.dryhome.service.dto.OrderItemDTO;
import uk.co.dryhome.service.mapper.CustomerOrderMapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    /**
     * Save a customerOrder.
     *
     * @param customerOrderDetailDTO the entity to save
     * @return the persisted entity
     */
    public CustomerOrderDetailDTO save(CustomerOrderDetailDTO customerOrderDetailDTO) {
        log.debug("Request to save CustomerOrder : {}", customerOrderDetailDTO);
        final CustomerOrder customerOrder = customerOrderMapper.detailToEntity(customerOrderDetailDTO);

        List<OrderItemDTO> newItems = customerOrderDetailDTO.getItems();
        List<OrderItem> oldItems = orderItemRepository.findByCustomerOrderIdOrderById(customerOrderDetailDTO.getId());
        Map<Long, OrderItem> itemsById = oldItems.stream().collect(Collectors.toMap(OrderItem::getId, Function.identity()));

        CustomerOrder savedCustomerOrder = customerOrderRepository.save(customerOrder);

        newItems.forEach(item -> {
            if (item.getId() != null) {
                OrderItem existingItem = itemsById.get(item.getId());
                if (existingItem != null) {
                    //existing item
                    existingItem.setProduct(productRepository.findById(item.getProductId()).orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId())));
                    existingItem.setPrice(item.getPrice());
                    existingItem.setQuantity(item.getQuantity());
                    existingItem.setNotes(item.getNotes());
                    existingItem.setSerialNumber(item.getSerialNumber());
                    log.info("Updating item: {}", existingItem);
                    orderItemRepository.save(existingItem);
                } else {
                    throw new RuntimeException("attempting to edit unknown item: " + item.getId());
                }
            } else {
                //new item
                OrderItem newItem = new OrderItem();
                newItem.setProduct(productRepository.findById(item.getProductId()).orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId())));
                newItem.setCustomerOrder(savedCustomerOrder);
                newItem.setPrice(item.getPrice());
                newItem.setQuantity(item.getQuantity());
                newItem.setSerialNumber(item.getSerialNumber());
                newItem.setNotes(item.getNotes());
                log.info("Adding items: {}", newItem);
                orderItemRepository.save(newItem);
            }
        });

        List<OrderItem> itemsToDelete = oldItems.stream()
            .filter(x -> !newItems.stream()
                .map(OrderItemDTO::getId)
                .collect(Collectors.toList())
                .contains(x.getId()))
            .collect(Collectors.toList());

        log.info("Deleting items: {}", itemsToDelete);
        orderItemRepository.deleteAll(itemsToDelete);

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
    public Page<CustomerOrderDTO> findAll(Pageable pageable) {
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
