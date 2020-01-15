package uk.co.dryhome.web.rest;

import com.google.common.collect.ImmutableSet;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import uk.co.dryhome.domain.MergeDocumentSource;
import uk.co.dryhome.service.ManualInvoiceService;
import uk.co.dryhome.service.MergeDocService;
import uk.co.dryhome.service.dto.ManualInvoiceDTO;
import uk.co.dryhome.service.dto.ManualInvoiceDetailDTO;
import uk.co.dryhome.web.rest.errors.BadRequestAlertException;
import uk.co.dryhome.web.rest.util.HeaderUtil;
import uk.co.dryhome.web.rest.util.PaginationUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing ManualInvoice.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class ManualInvoiceResource{
    private static final String ENTITY_NAME = "manualInvoice";

    private final ManualInvoiceService manualInvoiceService;

    /**
     * POST  /manual-invoices : Create a new manualInvoice.
     *
     * @param manualInvoiceDetailDTO the manualInvoiceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new manualInvoiceDTO, or with status 400 (Bad Request) if the manualInvoice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/manual-invoices")
    public ResponseEntity<ManualInvoiceDetailDTO> createManualInvoice(@Valid @RequestBody ManualInvoiceDetailDTO manualInvoiceDetailDTO) throws URISyntaxException {
        log.debug("REST request to save ManualInvoice : {}", manualInvoiceDetailDTO);
        if (manualInvoiceDetailDTO.getId() != null) {
            throw new BadRequestAlertException("A new manualInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ManualInvoiceDetailDTO result = manualInvoiceService.save(manualInvoiceDetailDTO);
        return ResponseEntity.created(new URI("/api/manual-invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /manual-invoices : Updates an existing manualInvoice.
     *
     * @param manualInvoiceDetailDTO the manualInvoiceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated manualInvoiceDTO,
     * or with status 400 (Bad Request) if the manualInvoiceDTO is not valid,
     * or with status 500 (Internal Server Error) if the manualInvoiceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/manual-invoices")
    public ResponseEntity<ManualInvoiceDetailDTO> updateManualInvoice(@Valid @RequestBody ManualInvoiceDetailDTO manualInvoiceDetailDTO) throws URISyntaxException {
        log.debug("REST request to update ManualInvoice : {}", manualInvoiceDetailDTO);
        if (manualInvoiceDetailDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ManualInvoiceDetailDTO result = manualInvoiceService.save(manualInvoiceDetailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, manualInvoiceDetailDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /manual-invoices : get all the manualInvoices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of manualInvoices in body
     */
    @GetMapping("/manual-invoices")
    public ResponseEntity<List<ManualInvoiceDTO>> getAllManualInvoices(Pageable pageable) {
        log.debug("REST request to get a page of ManualInvoices");
        Page<ManualInvoiceDTO> page = manualInvoiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/manual-invoices");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /manual-invoices/:id : get the "id" manualInvoice.
     *
     * @param id the id of the manualInvoiceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the manualInvoiceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/manual-invoices/{id}")
    public ResponseEntity<ManualInvoiceDetailDTO> getManualInvoice(@PathVariable Long id) {
        log.debug("REST request to get ManualInvoice : {}", id);
        Optional<ManualInvoiceDetailDTO> manualInvoiceDTO = manualInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(manualInvoiceDTO);
    }

    /**
     * DELETE  /manual-invoices/:id : delete the "id" manualInvoice.
     *
     * @param id the id of the manualInvoiceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/manual-invoices/{id}")
    public ResponseEntity<Void> deleteManualInvoice(@PathVariable Long id) {
        log.debug("REST request to delete ManualInvoice : {}", id);
        manualInvoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/manual-invoices/{id}/document")
    public void document(@RequestParam String documentName, @PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to create document {} for manual invoice : {}", documentName, id);
        manualInvoiceService.createDocument(id, response, documentName);
    }


}
