package uk.co.dryhome.service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;
    private BigDecimal price;
    private Integer quantity;
    private String notes;
    private String serialNumber;
    private String product;
}
