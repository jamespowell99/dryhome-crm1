package uk.co.dryhome.service.mapper;

import uk.co.dryhome.domain.*;
import uk.co.dryhome.service.dto.CustomerOrderDTO;

import org.mapstruct.*;
import uk.co.dryhome.service.dto.CustomerOrderDetailDTO;
import uk.co.dryhome.service.dto.CustomerOrderSummaryDTO;

/**
 * Mapper for the entity CustomerOrder and its DTO CustomerOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface CustomerOrderMapper extends EntityMapper<CustomerOrderDTO, CustomerOrder> {

    @Mapping(source = "customer.id", target = "customerId")
    CustomerOrderDTO toDto(CustomerOrder customerOrder);

    @Mapping(target = "items", ignore = true)
    @Mapping(source = "customerId", target = "customer")
    CustomerOrder toEntity(CustomerOrderDTO customerOrderDTO);

    @Mapping(source = "customer.id", target = "customerId")
    CustomerOrderSummaryDTO toSummaryDto(CustomerOrder customerOrder);

    @Mapping(target = "items", ignore = true)
    @Mapping(source = "customer.id", target = "customerId")
    CustomerOrderDetailDTO toDetailDto(CustomerOrder customerOrder);

    default CustomerOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(id);
        return customerOrder;
    }
}
