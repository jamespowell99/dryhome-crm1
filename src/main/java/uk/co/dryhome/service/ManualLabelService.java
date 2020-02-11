package uk.co.dryhome.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.dryhome.domain.ManualLabel;
import uk.co.dryhome.repository.ManualLabelRepository;
import uk.co.dryhome.service.docs.DocTemplate;
import uk.co.dryhome.service.docs.DocTemplateFactory;
import uk.co.dryhome.service.docs.ManualLabelsDocTemplate;
import uk.co.dryhome.service.dto.ManualLabelDTO;
import uk.co.dryhome.service.mapper.ManualLabelMapper;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Service Implementation for managing ManualLabel.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ManualLabelService implements MergeDocSourceService {
    private final Logger log = LoggerFactory.getLogger(ManualLabelService.class);

    private final ManualLabelRepository manualLabelRepository;
    private final ManualLabelMapper manualLabelMapper;
    private final MergeDocService mergeDocService;

    /**
     * Save a manualLabel.
     *
     * @param manualLabelDTO the entity to save
     * @return the persisted entity
     */
    public ManualLabelDTO save(ManualLabelDTO manualLabelDTO) {
        log.debug("Request to save ManualLabel : {}", manualLabelDTO);
        ManualLabel manualLabel = manualLabelMapper.toEntity(manualLabelDTO);
        manualLabel = manualLabelRepository.save(manualLabel);
        ManualLabelDTO result = manualLabelMapper.toDto(manualLabel);
        return result;
    }

    /**
     * Get all the manualLabels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ManualLabelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ManualLabels");
        return manualLabelRepository.findAll(pageable)
            .map(manualLabelMapper::toDto);
    }


    /**
     * Get one manualLabel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ManualLabelDTO> findOne(Long id) {
        log.debug("Request to get ManualLabel : {}", id);
        return manualLabelRepository.findById(id)
            .map(manualLabelMapper::toDto);
    }

    /**
     * Delete the manualLabel by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ManualLabel : {}", id);
        manualLabelRepository.deleteById(id);
    }

    @Override
    public void createDocument(Long id, HttpServletResponse response, String templateName, DocPrintType docPrintType) {
        DocTemplate template = DocTemplateFactory.fromTemplateName(ManualLabelsDocTemplate.class, templateName);
        mergeDocService.generateDocument(template, docPrintType, response, manualLabelRepository.getOne(id));
    }
}
