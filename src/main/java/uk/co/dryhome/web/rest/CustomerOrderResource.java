package uk.co.dryhome.web.rest;
import uk.co.dryhome.service.CustomerOrderService;
import uk.co.dryhome.service.dto.CustomerOrderDetailDTO;
import uk.co.dryhome.service.dto.CustomerOrderSummaryDTO;
import uk.co.dryhome.web.rest.errors.BadRequestAlertException;
import uk.co.dryhome.web.rest.util.HeaderUtil;
import uk.co.dryhome.web.rest.util.PaginationUtil;
import uk.co.dryhome.service.dto.CustomerOrderDTO;
import uk.co.dryhome.service.dto.CustomerOrderCriteria;
import uk.co.dryhome.service.CustomerOrderQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CustomerOrder.
 */
@RestController
@RequestMapping("/api")
public class CustomerOrderResource {

    private final Logger log = LoggerFactory.getLogger(CustomerOrderResource.class);

    private static final String ENTITY_NAME = "customerOrder";

    private final CustomerOrderService customerOrderService;

    private final CustomerOrderQueryService customerOrderQueryService;

    public CustomerOrderResource(CustomerOrderService customerOrderService, CustomerOrderQueryService customerOrderQueryService) {
        this.customerOrderService = customerOrderService;
        this.customerOrderQueryService = customerOrderQueryService;
    }

    /**
     * POST  /customer-orders : Create a new customerOrder.
     *
     * @param customerOrderDTO the customerOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerOrderDTO, or with status 400 (Bad Request) if the customerOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customer-orders")
    public ResponseEntity<CustomerOrderDTO> createCustomerOrder(@Valid @RequestBody CustomerOrderDTO customerOrderDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerOrder : {}", customerOrderDTO);
        if (customerOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerOrderDTO result = customerOrderService.save(customerOrderDTO);
        return ResponseEntity.created(new URI("/api/customer-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-orders : Updates an existing customerOrder.
     *
     * @param customerOrderDTO the customerOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerOrderDTO,
     * or with status 400 (Bad Request) if the customerOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customer-orders")
    public ResponseEntity<CustomerOrderDTO> updateCustomerOrder(@Valid @RequestBody CustomerOrderDTO customerOrderDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerOrder : {}", customerOrderDTO);
        if (customerOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomerOrderDTO result = customerOrderService.save(customerOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-orders : get all the customerOrders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of customerOrders in body
     */
    @GetMapping("/customer-orders")
    public ResponseEntity<List<CustomerOrderSummaryDTO>> getAllCustomerOrders(CustomerOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CustomerOrders by criteria: {}", criteria);
        Page<CustomerOrderSummaryDTO> page = customerOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-orders");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /customer-orders/count : count all the customerOrders.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/customer-orders/count")
    public ResponseEntity<Long> countCustomerOrders(CustomerOrderCriteria criteria) {
        log.debug("REST request to count CustomerOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerOrderQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /customer-orders/:id : get the "id" customerOrder.
     *
     * @param id the id of the customerOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customer-orders/{id}")
    public ResponseEntity<CustomerOrderDetailDTO> getCustomerOrder(@PathVariable Long id) {
        log.debug("REST request to get CustomerOrder : {}", id);
        Optional<CustomerOrderDetailDTO> customerOrderDTO = customerOrderQueryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerOrderDTO);
    }

    /**
     * DELETE  /customer-orders/:id : delete the "id" customerOrder.
     *
     * @param id the id of the customerOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customer-orders/{id}")
    public ResponseEntity<Void> deleteCustomerOrder(@PathVariable Long id) {
        log.debug("REST request to delete CustomerOrder : {}", id);
        customerOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/customer-orders?query=:query : search for the customerOrder corresponding
     * to the query.
     *
     * @param query the query of the customerOrder search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/customer-orders")
    public ResponseEntity<List<CustomerOrderDTO>> searchCustomerOrders(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CustomerOrders for query {}", query);
        Page<CustomerOrderDTO> page = customerOrderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customer-orders");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
