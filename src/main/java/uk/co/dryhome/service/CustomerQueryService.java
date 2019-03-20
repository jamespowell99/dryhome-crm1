package uk.co.dryhome.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import uk.co.dryhome.domain.Customer;
import uk.co.dryhome.domain.*; // for static metamodels
import uk.co.dryhome.repository.CustomerRepository;
import uk.co.dryhome.repository.search.CustomerSearchRepository;
import uk.co.dryhome.service.dto.CustomerCriteria;
import uk.co.dryhome.service.dto.CustomerDTO;
import uk.co.dryhome.service.mapper.CustomerMapper;

/**
 * Service for executing complex queries for Customer entities in the database.
 * The main input is a {@link CustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerDTO} or a {@link Page} of {@link CustomerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerQueryService extends QueryService<Customer> {

    private final Logger log = LoggerFactory.getLogger(CustomerQueryService.class);

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final CustomerSearchRepository customerSearchRepository;

    public CustomerQueryService(CustomerRepository customerRepository, CustomerMapper customerMapper, CustomerSearchRepository customerSearchRepository) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.customerSearchRepository = customerSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CustomerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO> findByCriteria(CustomerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerMapper.toDto(customerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerDTO> findByCriteria(CustomerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.findAll(specification, page)
            .map(customerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.count(specification);
    }

    /**
     * Function to convert CustomerCriteria to a {@link Specification}
     */
    private Specification<Customer> createSpecification(CustomerCriteria criteria) {
        Specification<Customer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Customer_.id));
            }
            if (criteria.getCompanyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyName(), Customer_.companyName));
            }
            if (criteria.getAddress1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress1(), Customer_.address1));
            }
            if (criteria.getAddress2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress2(), Customer_.address2));
            }
            if (criteria.getAddress3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress3(), Customer_.address3));
            }
            if (criteria.getTown() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTown(), Customer_.town));
            }
            if (criteria.getPostCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostCode(), Customer_.postCode));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Customer_.title));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Customer_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Customer_.lastName));
            }
            if (criteria.getTel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTel(), Customer_.tel));
            }
            if (criteria.getMobile() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobile(), Customer_.mobile));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Customer_.email));
            }
            if (criteria.getProducts() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProducts(), Customer_.products));
            }
            if (criteria.getInterested() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInterested(), Customer_.interested));
            }
            if (criteria.getPaid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaid(), Customer_.paid));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Customer_.type));
            }
        }
        return specification;
    }
}
