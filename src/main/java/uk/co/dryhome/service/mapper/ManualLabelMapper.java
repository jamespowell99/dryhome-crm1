package uk.co.dryhome.service.mapper;

import uk.co.dryhome.domain.*;
import uk.co.dryhome.service.dto.ManualLabelDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ManualLabel and its DTO ManualLabelDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ManualLabelMapper extends EntityMapper<ManualLabelDTO, ManualLabel> {



    default ManualLabel fromId(Long id) {
        if (id == null) {
            return null;
        }
        ManualLabel manualLabel = new ManualLabel();
        manualLabel.setId(id);
        return manualLabel;
    }
}
