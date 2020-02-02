package uk.co.dryhome.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.dryhome.domain.CustomerOrderSums;
import uk.co.dryhome.domain.enumeration.OrderStatus;
import uk.co.dryhome.service.CustomerOrderQueryService;
import uk.co.dryhome.service.CustomerOrderService;
import uk.co.dryhome.service.dto.CustomerOrderCriteria;
import uk.co.dryhome.service.dto.CustomerOrderDetailDTO;
import uk.co.dryhome.service.dto.CustomerOrderReportDTO;
import uk.co.dryhome.service.dto.CustomerOrderSummaryDTO;
import uk.co.dryhome.web.rest.errors.BadRequestAlertException;
import uk.co.dryhome.web.rest.util.HeaderUtil;
import uk.co.dryhome.web.rest.util.PaginationUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.anyNotNull;

/**
 * REST controller for managing CustomerOrder.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerOrderResource {

    private final Logger log = LoggerFactory.getLogger(CustomerOrderResource.class);

    private static final String ENTITY_NAME = "customerOrder";

    private final CustomerOrderService customerOrderService;
    private final CustomerOrderQueryService customerOrderQueryService;

    /**
     * POST  /customer-orders : Create a new customerOrder.
     *
     * @param customerOrderDTO the customerOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerOrderDTO, or with status 400 (Bad Request) if the customerOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customer-orders")
    public ResponseEntity<CustomerOrderDetailDTO> createCustomerOrder(@Valid @RequestBody CustomerOrderDetailDTO customerOrderDetailDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerOrder : {}", customerOrderDetailDTO);
        if (customerOrderDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerOrderDetailDTO result = customerOrderService.create(customerOrderDetailDTO);
        return ResponseEntity.created(new URI("/api/customer-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-orders : Updates an existing customerOrder.
     *
     * @param CustomerOrderDetailDTO the CustomerOrderDetailDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerOrderDTO,
     * or with status 400 (Bad Request) if the customerOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customer-orders")
    public ResponseEntity<CustomerOrderDetailDTO> updateCustomerOrder(@Valid @RequestBody CustomerOrderDetailDTO customerOrderDetailDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerOrder : {}", customerOrderDetailDTO);
        if (customerOrderDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomerOrderDetailDTO result = customerOrderService.save(customerOrderDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerOrderDetailDTO.getId().toString()))
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

    @GetMapping("/customer-orders/report")
    public ResponseEntity<?> reportCustomerOrders(
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "statuses", required = false) List<OrderStatus> statuses,
            Pageable pageable) {
        log.debug("REST request to get CustomerOrder report by dates: {} - {}", startDate, endDate);

        if (!anyNotNull(startDate, endDate, statuses) ) {
            return ResponseEntity.badRequest().body("no parameters provided");
        }

        if ((startDate != null && endDate == null) || (endDate != null && startDate == null)) {
            return ResponseEntity.badRequest().body("date filter must contain start and end date");
        }

        Pair<CustomerOrderSums, Page<CustomerOrderSummaryDTO>> result = customerOrderQueryService.report(startDate, endDate, statuses, pageable);
        Page<CustomerOrderSummaryDTO> page = result.getRight();
        CustomerOrderSums sums = result.getLeft();
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-orders");
        return ResponseEntity.ok().headers(headers).body(new CustomerOrderReportDTO(sums.getCount(), sums.getSubTotal(), sums.getVatAmount(), sums.getTotal(), page.getContent()));
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

    @GetMapping("/customer-orders/{id}/document")
    public void document(@RequestParam String documentName, @PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to create document {} for customer order : {}", documentName, id);
        customerOrderQueryService.createDocument(id, response, documentName);
    }

}
