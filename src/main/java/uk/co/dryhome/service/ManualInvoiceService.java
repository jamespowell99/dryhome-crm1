package uk.co.dryhome.service;

import com.google.common.collect.ImmutableSet;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.dryhome.domain.ManualInvoice;
import uk.co.dryhome.domain.ManualInvoiceItem;
import uk.co.dryhome.repository.ManualInvoiceItemRepository;
import uk.co.dryhome.repository.ManualInvoiceRepository;
import uk.co.dryhome.service.docs.DocTemplate;
import uk.co.dryhome.service.docs.DocTemplateFactory;
import uk.co.dryhome.service.docs.ManualInvoiceDocTemplate;
import uk.co.dryhome.service.docs.ManualLabelsDocTemplate;
import uk.co.dryhome.service.dto.ManualInvoiceDTO;
import uk.co.dryhome.service.dto.ManualInvoiceDetailDTO;
import uk.co.dryhome.service.dto.ManualInvoiceItemDTO;
import uk.co.dryhome.service.mapper.ManualInvoiceMapper;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ManualInvoice.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ManualInvoiceService implements MergeDocSourceService {
    private final Logger log = LoggerFactory.getLogger(ManualInvoiceService.class);

    private final ManualInvoiceRepository manualInvoiceRepository;
    private final ManualInvoiceMapper manualInvoiceMapper;
    private final ManualInvoiceItemRepository manualInvoiceItemRepository;
    private final MergeDocService mergeDocService;

    public ManualInvoiceDetailDTO create(ManualInvoiceDetailDTO manualInvoiceDetailDTO) {
        log.debug("Request to create ManualInvoic3 : {}", manualInvoiceDetailDTO);
        final ManualInvoice manualInvoice = manualInvoiceMapper.detailToEntity(manualInvoiceDetailDTO);
        ManualInvoice savedManualInvoice = manualInvoiceRepository.save(manualInvoice);
        manualInvoice.getItems().forEach( i -> {
            i.setManualInvoice(savedManualInvoice);
            manualInvoiceItemRepository.save(i);
        });
        return manualInvoiceMapper.toDetailDto(savedManualInvoice);

    }

    /**
     * Save a manualInvoice.
     *
     * @param manualInvoiceDetailDTO the entity to save
     * @return the persisted entity
     */
    public ManualInvoiceDetailDTO save(ManualInvoiceDetailDTO manualInvoiceDetailDTO) {
        log.debug("Request to save ManualInvoice : {}", manualInvoiceDetailDTO);
        ManualInvoice existingManualInvoice = manualInvoiceRepository.findById(manualInvoiceDetailDTO.getId()).orElseThrow(() -> new RuntimeException("invoice not found: " + manualInvoiceDetailDTO.getId()));
        Set<ManualInvoiceItem> oldItems = existingManualInvoice.getItems();

        final ManualInvoice newManualInvoice = manualInvoiceMapper.detailToEntity(manualInvoiceDetailDTO);
        Set<ManualInvoiceItem> newItems = newManualInvoice.getItems();
        newItems.removeIf( x-> x.getId() == null);

        //Update existing items
        newItems.forEach( i ->{
            ManualInvoiceItem existingItem = oldItems.stream()
                .filter(oi -> oi.getId().equals(i.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("attempting to edit unknown item: " + i.getId()));
            //existing item
            existingItem.setProduct(i.getProduct());
            existingItem.setPrice(i.getPrice());
            existingItem.setQuantity(i.getQuantity());
            log.info("Updating item: {}", existingItem);
            manualInvoiceItemRepository.save(existingItem);
        });

        //add new items
        manualInvoiceDetailDTO.getItems().stream()
            .filter(i -> i.getId() == null)
            .map(manualInvoiceMapper::itemToEntity)
            .map(ie -> ie.manualInvoice(newManualInvoice))
            .forEach(ie -> {
                manualInvoiceItemRepository.save(ie);
                newManualInvoice.addItems(ie);
            });

        //delete old items
        List<ManualInvoiceItem> itemsToDelete = oldItems.stream()
            .filter(x -> !manualInvoiceDetailDTO.getItems().stream()
                .map(ManualInvoiceItemDTO::getId)
                .collect(Collectors.toList())
                .contains(x.getId()))
            .collect(Collectors.toList());
        log.info("Deleting items: {}", itemsToDelete);
        manualInvoiceItemRepository.deleteAll(itemsToDelete);

        ManualInvoice savedManualInvoice = manualInvoiceRepository.save(newManualInvoice);

        ManualInvoiceDetailDTO result = manualInvoiceMapper.toDetailDto(savedManualInvoice);
        return result;
    }

    /**
     * Get all the manualInvoices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ManualInvoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ManualInvoices");
        return manualInvoiceRepository.findAll(pageable)
            .map(x -> manualInvoiceMapper.toDto(x));
    }


    /**
     * Get one manualInvoice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ManualInvoiceDetailDTO> findOne(Long id) {
        log.debug("Request to get ManualInvoice : {}", id);
        return manualInvoiceRepository.findById(id)
            .map(manualInvoiceMapper::toDetailDto);
    }

    /**
     * Delete the manualInvoice by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ManualInvoice : {}", id);
        manualInvoiceItemRepository.deleteByManualInvoiceId(id);
        manualInvoiceRepository.deleteById(id);
    }

    @Override
    public void createDocument(Long id, HttpServletResponse response, String templateName, DocPrintType docPrintType) {
        DocTemplate template = DocTemplateFactory.fromTemplateName(ManualInvoiceDocTemplate.class, templateName);
        mergeDocService.generateDocument(template, docPrintType, response, manualInvoiceRepository.getOne(id));
    }
}
