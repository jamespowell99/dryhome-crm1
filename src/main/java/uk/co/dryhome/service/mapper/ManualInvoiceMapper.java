package uk.co.dryhome.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.co.dryhome.domain.ManualInvoice;
import uk.co.dryhome.domain.ManualInvoiceItem;
import uk.co.dryhome.service.dto.ManualInvoiceDTO;
import uk.co.dryhome.service.dto.ManualInvoiceDetailDTO;
import uk.co.dryhome.service.dto.ManualInvoiceItemDTO;

/**
 * Mapper for the entity ManualInvoice and its DTO ManualInvoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ManualInvoiceMapper extends EntityMapper<ManualInvoiceDTO, ManualInvoice> {

    @Mapping(target = "items", ignore = true)
    ManualInvoice toEntity(ManualInvoiceDTO manualInvoiceDTO);


    @Mapping(target = "items", ignore = true)
    ManualInvoice detailToEntity(ManualInvoiceDetailDTO manualInvoiceDTO);

    ManualInvoiceDetailDTO toDetailDto(ManualInvoice manualInvoice);

    ManualInvoiceItemDTO itemToDto(ManualInvoiceItem item);

    default ManualInvoice fromId(Long id) {
        if (id == null) {
            return null;
        }
        ManualInvoice manualInvoice = new ManualInvoice();
        manualInvoice.setId(id);
        return manualInvoice;
    }
}
