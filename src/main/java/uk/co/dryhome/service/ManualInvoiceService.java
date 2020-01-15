package uk.co.dryhome.service;

import com.google.common.collect.ImmutableSet;
import com.powtechconsulting.mailmerge.WordMerger;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.co.dryhome.domain.ManualInvoice;
import uk.co.dryhome.domain.ManualInvoiceItem;
import uk.co.dryhome.domain.MergeDocumentSource;
import uk.co.dryhome.repository.ManualInvoiceItemRepository;
import uk.co.dryhome.repository.ManualInvoiceRepository;
import uk.co.dryhome.service.dto.ManualInvoiceDTO;
import uk.co.dryhome.service.dto.ManualInvoiceDetailDTO;
import uk.co.dryhome.service.dto.ManualInvoiceItemDTO;
import uk.co.dryhome.service.mapper.ManualInvoiceMapper;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ManualInvoice.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ManualInvoiceService implements MergeDocSourceService {
    private final static Set<String> ALLOWED_DOCUMENTS =
        ImmutableSet.of("customer-invoice", "accountant-invoice", "file-invoice");
    private final Logger log = LoggerFactory.getLogger(ManualInvoiceService.class);

    private final ManualInvoiceRepository manualInvoiceRepository;
    private final ManualInvoiceMapper manualInvoiceMapper;
    private final ManualInvoiceItemRepository manualInvoiceItemRepository;
    private final MergeDocService mergeDocService;

    /**
     * Save a manualInvoice.
     *
     * @param manualInvoiceDetailDTO the entity to save
     * @return the persisted entity
     */
    public ManualInvoiceDetailDTO save(ManualInvoiceDetailDTO manualInvoiceDetailDTO) {
        log.debug("Request to save ManualInvoice : {}", manualInvoiceDetailDTO);
        ManualInvoice manualInvoice = manualInvoiceMapper.detailToEntity(manualInvoiceDetailDTO);

        List<ManualInvoiceItemDTO> newItems = manualInvoiceDetailDTO.getItems();
        List<ManualInvoiceItem> oldItems = manualInvoiceItemRepository.findByManualInvoiceIdOrderById(manualInvoiceDetailDTO.getId());
        Map<Long, ManualInvoiceItem> itemsById = oldItems.stream().collect(Collectors.toMap(ManualInvoiceItem::getId, Function.identity()));

        ManualInvoice savedManualInvoice = manualInvoiceRepository.save(manualInvoice);

        newItems.forEach(item -> {
            if (item.getId() != null) {
                ManualInvoiceItem existingItem = itemsById.get(item.getId());
                if (existingItem != null) {
                    //existing item
                    existingItem.setProduct(item.getProduct());
                    existingItem.setPrice(item.getPrice());
                    existingItem.setQuantity(item.getQuantity());
                    log.info("Updating item: {}", existingItem);
                    manualInvoiceItemRepository.save(existingItem);
                } else {
                    throw new RuntimeException("attempting to edit unknown item: " + item.getId());
                }
            } else {
                //new item
                ManualInvoiceItem newItem = new ManualInvoiceItem();
                newItem.setProduct(item.getProduct());
                newItem.setManualInvoice(savedManualInvoice);
                newItem.setPrice(item.getPrice());
                newItem.setQuantity(item.getQuantity());
                log.info("Adding items: {}", newItem);
                manualInvoiceItemRepository.save(newItem);
            }
        });

        List<ManualInvoiceItem> itemsToDelete = oldItems.stream()
            .filter(x -> !newItems.stream()
                .map(ManualInvoiceItemDTO::getId)
                .collect(Collectors.toList())
                .contains(x.getId()))
            .collect(Collectors.toList());

        log.info("Deleting items: {}", itemsToDelete);
        manualInvoiceItemRepository.deleteAll(itemsToDelete);

        ManualInvoiceDetailDTO result = manualInvoiceMapper.toDetailDto(manualInvoice);
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
            .map(x -> {
                ManualInvoiceDTO manualInvoiceDTO = manualInvoiceMapper.toDto(x);
                List<ManualInvoiceItem> orderItems = manualInvoiceItemRepository.findByManualInvoiceIdOrderById(x.getId());
                BigDecimal orderSubTotal = orderItems.stream()
                    .map(oi -> oi.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal vatAmount = orderSubTotal.multiply(x.getVatRate().divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP));
                manualInvoiceDTO.setOrderTotal(orderSubTotal.add(vatAmount));
                return manualInvoiceDTO;
            });
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
            .map(x -> {
                ManualInvoiceDetailDTO manualInvoiceDetailDTO = manualInvoiceMapper.toDetailDto(x);

                List<ManualInvoiceItem> invoiceItems = manualInvoiceItemRepository.findByManualInvoiceIdOrderById(x.getId());
                List<ManualInvoiceItemDTO> items = invoiceItems.stream().map(oi -> {
                    //todo use mapper?
                    ManualInvoiceItemDTO itemDto = new ManualInvoiceItemDTO();
                    itemDto.setId(oi.getId());
                    itemDto.setPrice(oi.getPrice());
                    itemDto.setQuantity(oi.getQuantity());
                    itemDto.setProduct(oi.getProduct());
                    return itemDto;
                }).collect(Collectors.toList());
                manualInvoiceDetailDTO.setItems(items);

                manualInvoiceDetailDTO.setOrderSubTotal(x.getSubTotal());
                manualInvoiceDetailDTO.setVatAmount(x.getVatAmount());
                manualInvoiceDetailDTO.setOrderTotal(x.getTotal());

                return manualInvoiceDetailDTO;
            });
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
    public void createDocument(Long id, HttpServletResponse response, String documentName) {
        mergeDocService.generateDocument(documentName, response, ALLOWED_DOCUMENTS, manualInvoiceRepository.getOne(id));
    }
}
