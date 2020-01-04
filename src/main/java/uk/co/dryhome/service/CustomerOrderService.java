package uk.co.dryhome.service;

import uk.co.dryhome.domain.CustomerOrder;
import uk.co.dryhome.repository.CustomerOrderRepository;
import uk.co.dryhome.repository.search.CustomerOrderSearchRepository;
import uk.co.dryhome.service.dto.CustomerOrderDTO;
import uk.co.dryhome.service.dto.CustomerOrderDetailDTO;
import uk.co.dryhome.service.mapper.CustomerOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CustomerOrder.
 */
@Service
@Transactional
public class CustomerOrderService {

    private final Logger log = LoggerFactory.getLogger(CustomerOrderService.class);

    private final CustomerOrderRepository customerOrderRepository;

    private final CustomerOrderMapper customerOrderMapper;

    private final CustomerOrderSearchRepository customerOrderSearchRepository;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepository, CustomerOrderMapper customerOrderMapper, CustomerOrderSearchRepository customerOrderSearchRepository) {
        this.customerOrderRepository = customerOrderRepository;
        this.customerOrderMapper = customerOrderMapper;
        this.customerOrderSearchRepository = customerOrderSearchRepository;
    }

    /**
     * Save a customerOrder.
     *
     * @param customerOrderDTO the entity to save
     * @return the persisted entity
     */
    public CustomerOrderDTO save(CustomerOrderDTO customerOrderDTO) {
        log.debug("Request to save CustomerOrder : {}", customerOrderDTO);
        CustomerOrder customerOrder = customerOrderMapper.toEntity(customerOrderDTO);
        customerOrder = customerOrderRepository.save(customerOrder);
        CustomerOrderDTO result = customerOrderMapper.toDto(customerOrder);
        customerOrderSearchRepository.save(customerOrder);
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
        customerOrderRepository.deleteById(id);
        customerOrderSearchRepository.deleteById(id);
    }

    /**
     * Search for the customerOrder corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CustomerOrderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerOrders for query {}", query);
        return customerOrderSearchRepository.search(queryStringQuery(query), pageable)
            .map(customerOrderMapper::toDto);
    }
}
