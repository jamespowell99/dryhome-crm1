package uk.co.dryhome.service;

import com.google.common.collect.ImmutableSet;
import com.powtechconsulting.mailmerge.WordMerger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.dryhome.domain.Customer;
import uk.co.dryhome.repository.CustomerRepository;
import uk.co.dryhome.service.dto.CustomerDTO;
import uk.co.dryhome.service.mapper.CustomerMapper;

import java.util.Optional;
import java.util.Set;


/**
 * Service Implementation for managing Customer.
 */
@Service
@Transactional
public class CustomerService {
    private final static Set<String> ALLOWED_DOCUMENTS =
        ImmutableSet.of("dp-record", "remcon-prod-lit", "dom-record", "labels");


    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final MergeDocService mergeDocService;


    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, MergeDocService mergeDocService) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.mergeDocService = mergeDocService;
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
    }

    public byte[] generateDocument(String documentName, Long id) {
        log.debug("Request to create document {} for Customer : {}", documentName, id);

        if (!ALLOWED_DOCUMENTS.contains(documentName)) {
            throw new RuntimeException("unrecognised document name: " + documentName);
        }

        Customer customer = customerRepository.getOne(id);
        return new WordMerger().merge(mergeDocService.getFile(documentName + ".docx"), customer.documentMappings());
    }

}
