package uk.co.dryhome.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.powtechconsulting.mailmerge.WordMerger;
import uk.co.dryhome.domain.Customer;
import uk.co.dryhome.repository.CustomerRepository;
import uk.co.dryhome.repository.search.CustomerSearchRepository;
import uk.co.dryhome.service.dto.CustomerDTO;
import uk.co.dryhome.service.mapper.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Optional;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Customer.
 */
@Service
@Transactional
public class CustomerService {
    private final static Set<String> ALLOWED_DOCUMENTS =
        ImmutableSet.of("dp-record", "remcon-prod-lit", "dom-record");


    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final CustomerSearchRepository customerSearchRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, CustomerSearchRepository customerSearchRepository) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.customerSearchRepository = customerSearchRepository;
    }

    /**
     * Save a customer.
     *
     * @param customerDTO the entity to save
     * @return the persisted entity
     */
    public CustomerDTO save(CustomerDTO customerDTO) {
        log.debug("Request to save Customer : {}", customerDTO);
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        CustomerDTO result = customerMapper.toDto(customer);
        customerSearchRepository.save(customer);
        return result;
    }

    /**
     * Get all the customers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CustomerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Customers");
        return customerRepository.findAll(pageable)
            .map(customerMapper::toDto);
    }


    /**
     * Get one customer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CustomerDTO> findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        return customerRepository.findById(id)
            .map(customerMapper::toDto);
    }

    /**
     * Delete the customer by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Customer : {}", id);
        customerRepository.deleteById(id);
        customerSearchRepository.deleteById(id);
    }

    /**
     * Search for the customer corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CustomerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Customers for query {}", query);
        return customerSearchRepository.search(queryStringQuery(query), pageable)
            .map(customerMapper::toDto);
    }

    public byte[] generateDocument(String documentName, Long id) {
        log.debug("Request to create document {} for Customer : {}", documentName, id);

        if (!ALLOWED_DOCUMENTS.contains(documentName)) {
            throw new RuntimeException("unrecognised document name: " + documentName);
        }

        String name = documentName + ".docx";
        URL resource = this.getClass().getClassLoader().getResource("document-templates/" + name);
        if (resource == null ) {
            throw new RuntimeException("file not found: " + name);
        }
        String fileName = resource.getFile();
        Customer customer = customerRepository.getOne(id);
        return new WordMerger().merge(fileName, customer.documentMappings());
    }
}
