package uk.co.dryhome.service.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomerOrderReportDTO {
    private int count;
    private BigDecimal subTotal;
    private BigDecimal vatAmount;
    private BigDecimal total;
    private List<CustomerOrderSummaryDTO> orders = new ArrayList<>();

    public CustomerOrderReportDTO(int count, BigDecimal subTotal, BigDecimal vatAmount, BigDecimal total, List<CustomerOrderSummaryDTO> orders) {
        this.count = count;
        this.subTotal = subTotal;
        this.vatAmount = vatAmount;
        this.total = total;
        this.orders = orders;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<CustomerOrderSummaryDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<CustomerOrderSummaryDTO> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "CustomerOrderReportDTO{" +
            "count=" + count +
            ", subTotal=" + subTotal +
            ", vatAmount=" + vatAmount +
            ", total=" + total +
            ", orders=" + orders +
            '}';
    }
}
