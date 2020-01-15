package uk.co.dryhome.web.rest;
import com.google.common.collect.ImmutableSet;
import lombok.RequiredArgsConstructor;
import uk.co.dryhome.service.ManualLabelService;
import uk.co.dryhome.service.MergeDocService;
import uk.co.dryhome.web.rest.errors.BadRequestAlertException;
import uk.co.dryhome.web.rest.util.HeaderUtil;
import uk.co.dryhome.web.rest.util.PaginationUtil;
import uk.co.dryhome.service.dto.ManualLabelDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing ManualLabel.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ManualLabelResource {
    private final Logger log = LoggerFactory.getLogger(ManualLabelResource.class);

    private static final String ENTITY_NAME = "manualLabel";

    private final ManualLabelService manualLabelService;

    /**
     * POST  /manual-labels : Create a new manualLabel.
     *
     * @param manualLabelDTO the manualLabelDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new manualLabelDTO, or with status 400 (Bad Request) if the manualLabel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/manual-labels")
    public ResponseEntity<ManualLabelDTO> createManualLabel(@Valid @RequestBody ManualLabelDTO manualLabelDTO) throws URISyntaxException {
        log.debug("REST request to save ManualLabel : {}", manualLabelDTO);
        if (manualLabelDTO.getId() != null) {
            throw new BadRequestAlertException("A new manualLabel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ManualLabelDTO result = manualLabelService.save(manualLabelDTO);
        return ResponseEntity.created(new URI("/api/manual-labels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /manual-labels : Updates an existing manualLabel.
     *
     * @param manualLabelDTO the manualLabelDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated manualLabelDTO,
     * or with status 400 (Bad Request) if the manualLabelDTO is not valid,
     * or with status 500 (Internal Server Error) if the manualLabelDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/manual-labels")
    public ResponseEntity<ManualLabelDTO> updateManualLabel(@Valid @RequestBody ManualLabelDTO manualLabelDTO) throws URISyntaxException {
        log.debug("REST request to update ManualLabel : {}", manualLabelDTO);
        if (manualLabelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ManualLabelDTO result = manualLabelService.save(manualLabelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, manualLabelDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /manual-labels : get all the manualLabels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of manualLabels in body
     */
    @GetMapping("/manual-labels")
    public ResponseEntity<List<ManualLabelDTO>> getAllManualLabels(Pageable pageable) {
        log.debug("REST request to get a page of ManualLabels");
        Page<ManualLabelDTO> page = manualLabelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/manual-labels");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /manual-labels/:id : get the "id" manualLabel.
     *
     * @param id the id of the manualLabelDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the manualLabelDTO, or with status 404 (Not Found)
     */
    @GetMapping("/manual-labels/{id}")
    public ResponseEntity<ManualLabelDTO> getManualLabel(@PathVariable Long id) {
        log.debug("REST request to get ManualLabel : {}", id);
        Optional<ManualLabelDTO> manualLabelDTO = manualLabelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(manualLabelDTO);
    }

    /**
     * DELETE  /manual-labels/:id : delete the "id" manualLabel.
     *
     * @param id the id of the manualLabelDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/manual-labels/{id}")
    public ResponseEntity<Void> deleteManualLabel(@PathVariable Long id) {
        log.debug("REST request to delete ManualLabel : {}", id);
        manualLabelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/manual-labels/{id}/document")
    public void document(@RequestParam String documentName, @PathVariable Long id, HttpServletResponse response ) {
        log.debug("REST request to create document {} for customer : {}", documentName, id);
        manualLabelService.createDocument(id, response, documentName);
    }

}
