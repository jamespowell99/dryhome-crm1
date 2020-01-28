package uk.co.dryhome.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.co.dryhome.domain.CustomerOrder;
import uk.co.dryhome.domain.OrderItem;
import uk.co.dryhome.service.dto.CustomerOrderDetailDTO;
import uk.co.dryhome.service.dto.CustomerOrderSummaryDTO;
import uk.co.dryhome.service.dto.OrderItemDTO;

/**
 * Mapper for the entity CustomerOrder and its DTO CustomerOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, ProductMapper.class})
public interface CustomerOrderMapper extends EntityMapper<CustomerOrderSummaryDTO, CustomerOrder> {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "customer.type", target = "customerType")
    CustomerOrderSummaryDTO toDto(CustomerOrder customerOrder);

    @Mapping(source = "customerId", target = "customer")
    CustomerOrder detailToEntity(CustomerOrderDetailDTO customerOrderDetailDTO);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "customer.type", target = "customerType")
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
