package uk.co.dryhome.service;

import com.amazonaws.services.cloudsearchdomain.model.SearchStatus;
import com.google.common.collect.ImmutableSet;
import io.github.jhipster.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.dryhome.domain.Customer;
import uk.co.dryhome.domain.CustomerOrder;
import uk.co.dryhome.domain.CustomerOrderSums;
import uk.co.dryhome.domain.CustomerOrder_;
import uk.co.dryhome.domain.Customer_;
import uk.co.dryhome.domain.OrderItem_;
import uk.co.dryhome.domain.enumeration.OrderStatus;
import uk.co.dryhome.repository.CustomerOrderRepository;
import uk.co.dryhome.service.dto.AddressDTO;
import uk.co.dryhome.service.dto.CustomerOrderCriteria;
import uk.co.dryhome.service.dto.CustomerOrderDetailDTO;
import uk.co.dryhome.service.dto.CustomerOrderStatsDTO;
import uk.co.dryhome.service.dto.CustomerOrderSummaryDTO;
import uk.co.dryhome.service.dto.Stat;
import uk.co.dryhome.service.dto.StatIndividual;
import uk.co.dryhome.service.mapper.CustomerOrderMapper;

import javax.persistence.criteria.JoinType;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * Service for executing complex queries for CustomerOrder entities in the database.
 * The main input is a {@link CustomerOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerOrderSummaryDTO} or a {@link Page} of {@link CustomerOrderSummaryDTO} which fulfills the criteria.
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
    private final MergeDocService mergeDocService;


    /**
     * Return a {@link List} of {@link CustomerOrderSummaryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerOrderSummaryDTO> findByCriteria(CustomerOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerOrder> specification = createSpecification(criteria);
        return customerOrderRepository.findAll(specification).stream()
            .map(customerOrderMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Return a {@link Page} of {@link CustomerOrderSummaryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerOrderSummaryDTO> findByCriteria(CustomerOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerOrder> specification = createSpecification(criteria);
        return customerOrderRepository.findAll(specification, page)
            .map(customerOrderMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Pair<CustomerOrderSums, Page<CustomerOrderSummaryDTO>> report(LocalDate startDate, LocalDate endDate, List<OrderStatus> statuses, Pageable page) {
        LocalDate searchStartDate = ofNullable(startDate).orElse(LocalDate.of(1900, 01, 01));
        LocalDate searchEndDate = ofNullable(endDate).orElse(LocalDate.of(3000, 01, 01));
        List<OrderStatus> searchStatuses = ofNullable(statuses).orElse(Arrays.asList(OrderStatus.values()));

        List<CustomerOrderSums> sums = customerOrderRepository.sumAmountsByOrderDateBetween(searchStartDate, searchEndDate, searchStatuses);
        if (sums.size() != 1) {
            throw new RuntimeException("sums size should be 1, was " + sums.size());
        }
        Page<CustomerOrderSummaryDTO> resultPage = customerOrderRepository.findByOrderDateBetweenAndStatusIn(searchStartDate, searchEndDate, searchStatuses, page)
            .map(customerOrderMapper::toDto);
        return new ImmutablePair<>(sums.get(0), resultPage);
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

    public CustomerOrderStatsDTO generateStats() {
        LocalDate now = LocalDate.now();

        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        Stat monthStat = getStats(
            now.withDayOfMonth(1),
            LocalDate.now().withDayOfMonth(now.lengthOfMonth()),
            now.minusMonths(1).withDayOfMonth(1),
            lastMonth.withDayOfMonth(lastMonth.lengthOfMonth())
            );

        Stat yearStats = getStats(
            now.withMonth(1).withDayOfMonth(1),
            now.withMonth(12).withDayOfMonth(31),
            now.minusYears(1).withMonth(1).withDayOfMonth(1),
            now.minusYears(1).withMonth(12).withDayOfMonth(31));

        Stat past12MonthStats = getStats(
            now.minusYears(1).plusDays(1),
            now,
            now.minusYears(2).plusDays(1),
            now.minusYears(1)
        );

        return new CustomerOrderStatsDTO(monthStat, yearStats, past12MonthStats);
    }

    private Stat getStats(LocalDate currentStart, LocalDate currentEnd, LocalDate previousStart, LocalDate previousEnd) {
        List<OrderStatus> allStatuses = Arrays.asList(OrderStatus.values());
        List<CustomerOrderSums> thisSums = customerOrderRepository.sumAmountsByOrderDateBetween(currentStart, currentEnd, allStatuses);
        List<CustomerOrderSums> lastSums = customerOrderRepository.sumAmountsByOrderDateBetween(previousStart, previousEnd, allStatuses);


        CustomerOrderSums thisVal = thisSums.get(0);
        StatIndividual current = new StatIndividual(thisVal.getCount(), thisVal.getTotal() == null ? BigDecimal.ZERO : thisVal.getTotal(), currentStart, currentEnd);

        CustomerOrderSums lastVal = lastSums.get(0);
        StatIndividual last = new StatIndividual(lastVal.getCount(), lastVal.getTotal() == null ? BigDecimal.ZERO : lastVal.getTotal(), previousStart, previousEnd);
        BigDecimal diff = current.getTotal().subtract(last.getTotal());
        return new Stat(current, last, diff);
    }
}
