package uk.co.dryhome.service.mapper;

import uk.co.dryhome.domain.*;
import uk.co.dryhome.service.dto.CustomerOrderDTO;

import org.mapstruct.*;
import uk.co.dryhome.service.dto.CustomerOrderDetailDTO;
import uk.co.dryhome.service.dto.CustomerOrderSummaryDTO;
import uk.co.dryhome.service.dto.OrderItemDTO;

/**
 * Mapper for the entity CustomerOrder and its DTO CustomerOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, ProductMapper.class})
public interface CustomerOrderMapper extends EntityMapper<CustomerOrderDTO, CustomerOrder> {

    @Mapping(source = "customer.id", target = "customerId")
    CustomerOrderDTO toDto(CustomerOrder customerOrder);

    @Mapping(target = "items", ignore = true)
    @Mapping(source = "customerId", target = "customer")
    CustomerOrder toEntity(CustomerOrderDTO customerOrderDTO);

    @Mapping(source = "customerId", target = "customer")
    CustomerOrder detailToEntity(CustomerOrderDetailDTO customerOrderDetailDTO);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    CustomerOrderSummaryDTO toSummaryDto(CustomerOrder customerOrder);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    CustomerOrderDetailDTO toDetailDto(CustomerOrder customerOrder);

    @Mapping(source = "product.name", target = "product")
    @Mapping(source = "product.id", target = "productId")
    OrderItemDTO itemToDto(OrderItem orderItem);

    @Mapping(source = "productId", target = "product")
    OrderItem itemToEntity(OrderItemDTO orderItemDTO);

    default CustomerOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(id);
        return customerOrder;
    }
}
