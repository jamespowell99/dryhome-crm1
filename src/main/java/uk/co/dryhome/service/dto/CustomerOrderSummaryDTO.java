package uk.co.dryhome.service.dto;

import uk.co.dryhome.domain.enumeration.CompanyType;
import uk.co.dryhome.domain.enumeration.OrderMethod;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the CustomerOrder entity.
 */
public class CustomerOrderSummaryDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 10)
    private String orderNumber;

    @NotNull
    private LocalDate orderDate;

    @Size(max = 100)
    private String notes1;

    @Size(max = 100)
    private String notes2;

    private LocalDate despatchDate;

    private LocalDate invoiceDate;

    private LocalDate paymentDate;

    @NotNull
    private BigDecimal vatRate;

    private String internalNotes;

    @Size(max = 10)
    private String invoiceNumber;

    @Size(max = 100)
    private String paymentStatus;

    @Size(max = 100)
    private String paymentType;

    private BigDecimal paymentAmount;

    @Size(max = 100)
    private String placedBy;

    private OrderMethod method;


    private Long customerId;

    private String customerName;

    private CompanyType customerType;

    private BigDecimal total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getNotes1() {
        return notes1;
    }

    public void setNotes1(String notes1) {
        this.notes1 = notes1;
    }

    public String getNotes2() {
        return notes2;
    }

    public void setNotes2(String notes2) {
        this.notes2 = notes2;
    }

    public LocalDate getDespatchDate() {
        return despatchDate;
    }

    public void setDespatchDate(LocalDate despatchDate) {
        this.despatchDate = despatchDate;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPlacedBy() {
        return placedBy;
    }

    public void setPlacedBy(String placedBy) {
        this.placedBy = placedBy;
    }

    public OrderMethod getMethod() {
        return method;
    }

    public void setMethod(OrderMethod method) {
        this.method = method;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public CompanyType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CompanyType customerType) {
        this.customerType = customerType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerOrderSummaryDTO customerOrderDTO = (CustomerOrderSummaryDTO) o;
        if (customerOrderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerOrderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerOrderSummaryDTO{" +
            "id=" + id +
            ", orderNumber='" + orderNumber + '\'' +
            ", orderDate=" + orderDate +
            ", notes1='" + notes1 + '\'' +
            ", notes2='" + notes2 + '\'' +
            ", despatchDate=" + despatchDate +
            ", invoiceDate=" + invoiceDate +
            ", paymentDate=" + paymentDate +
            ", vatRate=" + vatRate +
            ", internalNotes='" + internalNotes + '\'' +
            ", invoiceNumber='" + invoiceNumber + '\'' +
            ", paymentStatus='" + paymentStatus + '\'' +
            ", paymentType='" + paymentType + '\'' +
            ", paymentAmount=" + paymentAmount +
            ", placedBy='" + placedBy + '\'' +
            ", method=" + method +
            ", customerId=" + customerId +
            ", customerName='" + customerName + '\'' +
            ", customerType=" + customerType +
            ", total=" + total +
            '}';
    }
}
