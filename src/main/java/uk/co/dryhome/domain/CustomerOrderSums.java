package uk.co.dryhome.domain;

import java.math.BigDecimal;

public interface CustomerOrderSums {
    BigDecimal getSubTotal();
    BigDecimal getVatAmount();
    BigDecimal getTotal();
    int getCount();
}
