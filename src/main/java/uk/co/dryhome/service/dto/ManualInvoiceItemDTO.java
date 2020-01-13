package uk.co.dryhome.service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ManualInvoiceItemDTO {
    private Long id;
    private BigDecimal price;
    private Integer quantity;
    private String product;
}
