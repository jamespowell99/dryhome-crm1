package uk.co.dryhome.service.mapper;

import uk.co.dryhome.domain.*;
import uk.co.dryhome.service.dto.CustomerOrderDetailDTO;
import uk.co.dryhome.service.dto.ManualInvoiceDTO;

import org.mapstruct.*;
import uk.co.dryhome.service.dto.ManualInvoiceDetailDTO;

/**
 * Mapper for the entity ManualInvoice and its DTO ManualInvoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ManualInvoiceMapper extends EntityMapper<ManualInvoiceDTO, ManualInvoice> {


    @Mapping(target = "items", ignore = true)
    ManualInvoice toEntity(ManualInvoiceDTO manualInvoiceDTO);


    @Mapping(target = "items", ignore = true)
    ManualInvoice detailToEntity(ManualInvoiceDetailDTO manualInvoiceDTO);

    @Mapping(target = "items", ignore = true)
    ManualInvoiceDetailDTO toDetailDto(ManualInvoice manualInvoice);

    default ManualInvoice fromId(Long id) {
        if (id == null) {
            return null;
        }
        ManualInvoice manualInvoice = new ManualInvoice();
        manualInvoice.setId(id);
        return manualInvoice;
    }
}
