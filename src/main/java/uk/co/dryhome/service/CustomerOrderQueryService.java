package uk.co.dryhome.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.JoinType;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ImmutableSet;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import uk.co.dryhome.domain.CustomerOrder;
import uk.co.dryhome.domain.*; // for static metamodels
import uk.co.dryhome.repository.CustomerOrderRepository;
import uk.co.dryhome.repository.OrderItemRepository;
import uk.co.dryhome.service.dto.AddressDTO;
import uk.co.dryhome.service.dto.CustomerOrderCriteria;
import uk.co.dryhome.service.dto.CustomerOrderDTO;
import uk.co.dryhome.service.dto.CustomerOrderDetailDTO;
import uk.co.dryhome.service.dto.CustomerOrderSummaryDTO;
import uk.co.dryhome.service.dto.OrderItemDTO;
import uk.co.dryhome.service.mapper.CustomerOrderMapper;

/**
 * Service for executing complex queries for CustomerOrder entities in the database.
 * The main input is a {@link CustomerOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerOrderDTO} or a {@link Page} of {@link CustomerOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerOrderQueryService extends QueryService<CustomerOrder> implements MergeDocSourceService {
    private final static Set<String> ALLOWED_DOCUMENTS =
        ImmutableSet.of("customer-invoice", "accountant-invoice", "file-invoice");

    private final Logger log = LoggerFactory.getLogger(CustomerOrderQueryService.class);

    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerOrderMapper customerOrderMapper;
    private final OrderItemRepository orderItemRepository;
    private final MergeDocService mergeDocService;


    /**
     * Return a {@link List} of {@link CustomerOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerOrderDTO> findByCriteria(CustomerOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerOrder> specification = createSpecification(criteria);
        return customerOrderMapper.toDto(customerOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerOrderSummaryDTO> findByCriteria(CustomerOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerOrder> specification = createSpecification(criteria);
        return customerOrderRepository.findAll(specification, page)
            .map(x -> customerOrderMapper.toSummaryDto(x));
    }

    /**
     * Get one customerOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<CustomerOrderDetailDTO> findOne(Long id) {
        log.debug("Request to get CustomerOrder : {}", id);
        return customerOrderRepository.findById(id)
            .map( o -> {
                CustomerOrderDetailDTO customerOrderDetailDTO = customerOrderMapper.toDetailDto(o);

                Customer customer = o.getCustomer();
                //todo different contact?
                customerOrderDetailDTO.setInvoiceContact(customer.getFullContactName());
                customerOrderDetailDTO.setDeliveryContact(customer.getFullContactName());

                AddressDTO customerAddress = new AddressDTO();
                customerAddress.setAddress1(customer.getAddress1());
                customerAddress.setAddress2(customer.getAddress2());
                customerAddress.setAddress3(customer.getAddress3());
                customerAddress.setTown(customer.getTown());
                customerAddress.setPostCode(customer.getPostCode());
                //todo allow different address
                customerOrderDetailDTO.setInvoiceAddress(customerAddress);
                customerOrderDetailDTO.setDeliveryAddress(customerAddress);

                return customerOrderDetailDTO;});
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerOrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerOrder> specification = createSpecification(criteria);
        return customerOrderRepository.count(specification);
    }

    /**
     * Function to convert CustomerOrderCriteria to a {@link Specification}
     */
    private Specification<CustomerOrder> createSpecification(CustomerOrderCriteria criteria) {
        Specification<CustomerOrder> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CustomerOrder_.id));
            }
            if (criteria.getOrderNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderNumber(), CustomerOrder_.orderNumber));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), CustomerOrder_.orderDate));
            }
            if (criteria.getNotes1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes1(), CustomerOrder_.notes1));
            }
            if (criteria.getNotes2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes2(), CustomerOrder_.notes2));
            }
            if (criteria.getDespatchDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDespatchDate(), CustomerOrder_.despatchDate));
            }
            if (criteria.getInvoiceDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInvoiceDate(), CustomerOrder_.invoiceDate));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), CustomerOrder_.paymentDate));
            }
            if (criteria.getVatRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVatRate(), CustomerOrder_.vatRate));
            }
            if (criteria.getInternalNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInternalNotes(), CustomerOrder_.internalNotes));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), CustomerOrder_.invoiceNumber));
            }
            if (criteria.getPaymentStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentStatus(), CustomerOrder_.paymentStatus));
            }
            if (criteria.getPaymentType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentType(), CustomerOrder_.paymentType));
            }
            if (criteria.getPaymentAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentAmount(), CustomerOrder_.paymentAmount));
            }
            if (criteria.getPlacedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlacedBy(), CustomerOrder_.placedBy));
            }
            if (criteria.getMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getMethod(), CustomerOrder_.method));
            }
            if (criteria.getItemsId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemsId(),
                    root -> root.join(CustomerOrder_.items, JoinType.LEFT).get(OrderItem_.id)));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerId(),
                    root -> root.join(CustomerOrder_.customer, JoinType.LEFT).get(Customer_.id)));
            }
        }
        return specification;
    }

    @Override
    public void createDocument(Long id, HttpServletResponse response, String documentName) {
        mergeDocService.generateDocument(documentName, response, ALLOWED_DOCUMENTS, customerOrderRepository.getOne(id));
    }
}
