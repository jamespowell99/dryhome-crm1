package uk.co.dryhome.domain;

import java.math.BigDecimal;

public interface OrderSummary {
    Integer getCount();
    BigDecimal getPreVatTotal();
    BigDecimal getPostVatTotal();
}
