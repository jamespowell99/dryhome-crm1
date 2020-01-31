package uk.co.dryhome.service.dto;

import uk.co.dryhome.domain.enumeration.CompanyType;
import uk.co.dryhome.domain.enumeration.OrderMethod;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the CustomerOrder entity.
 */
public class CustomerOrderReportResponseDTO implements Serializable {

    private int totalOrders;
    private BigDecimal preVatTotal;
    private BigDecimal postVatTotal;

    private List<CustomerOrderSummaryDTO> orders = new ArrayList<>();

    public CustomerOrderReportResponseDTO(int totalOrders, BigDecimal preVatTotal, BigDecimal postVatTotal, List<CustomerOrderSummaryDTO> orders) {
        this.totalOrders = totalOrders;
        this.preVatTotal = preVatTotal;
        this.postVatTotal = postVatTotal;
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "CustomerOrderReportResponseDTO{" +
            "totalOrders=" + totalOrders +
            ", preVatTotal=" + preVatTotal +
            ", postVatTotal=" + postVatTotal +
            ", orders=" + orders +
            '}';
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getPreVatTotal() {
        return preVatTotal;
    }

    public void setPreVatTotal(BigDecimal preVatTotal) {
        this.preVatTotal = preVatTotal;
    }

    public BigDecimal getPostVatTotal() {
        return postVatTotal;
    }

    public void setPostVatTotal(BigDecimal postVatTotal) {
        this.postVatTotal = postVatTotal;
    }

    public List<CustomerOrderSummaryDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<CustomerOrderSummaryDTO> orders) {
        this.orders = orders;
    }
}
