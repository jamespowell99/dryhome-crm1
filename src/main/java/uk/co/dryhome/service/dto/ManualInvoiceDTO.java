package uk.co.dryhome.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the ManualInvoice entity.
 */
public class ManualInvoiceDTO implements Serializable {

    private Long id;

    @NotNull
    private String invoiceNumber;

    @NotNull
    private String orderNumber;

    @NotNull
    private LocalDate invoiceDate;

    private String ref;

    private String customer;

    private String address1;

    private String address2;

    private String address3;

    private String town;

    private String postCode;

    private String telNo;

    private String deliveryAddress1;

    private String deliveryAddress2;

    private String deliveryAddress3;

    private String deliveryAddress4;

    private String specialInstructions1;

    private String specialInstructions2;

    private LocalDate paymentDate;

    private String paymentStatus;

    private String paymentType;

    private BigDecimal paymentAmount;

    @NotNull
    private BigDecimal vatRate;

    private BigDecimal orderTotal;

    @Override
    public String toString() {
        return "ManualInvoiceDTO{" +
            "id=" + id +
            ", invoiceNumber='" + invoiceNumber + '\'' +
            ", orderNumber='" + orderNumber + '\'' +
            ", invoiceDate=" + invoiceDate +
            ", ref='" + ref + '\'' +
            ", customer='" + customer + '\'' +
            ", address1='" + address1 + '\'' +
            ", address2='" + address2 + '\'' +
            ", address3='" + address3 + '\'' +
            ", town='" + town + '\'' +
            ", postCode='" + postCode + '\'' +
            ", telNo='" + telNo + '\'' +
            ", deliveryAddress1='" + deliveryAddress1 + '\'' +
            ", deliveryAddress2='" + deliveryAddress2 + '\'' +
            ", deliveryAddress3='" + deliveryAddress3 + '\'' +
            ", deliveryAddress4='" + deliveryAddress4 + '\'' +
            ", specialInstructions1='" + specialInstructions1 + '\'' +
            ", specialInstructions2='" + specialInstructions2 + '\'' +
            ", paymentDate=" + paymentDate +
            ", paymentStatus='" + paymentStatus + '\'' +
            ", paymentType='" + paymentType + '\'' +
            ", paymentAmount=" + paymentAmount +
            ", vatRate=" + vatRate +
            ", orderTotal=" + orderTotal +
            '}';
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getDeliveryAddress1() {
        return deliveryAddress1;
    }

    public void setDeliveryAddress1(String deliveryAddress1) {
        this.deliveryAddress1 = deliveryAddress1;
    }

    public String getDeliveryAddress2() {
        return deliveryAddress2;
    }

    public void setDeliveryAddress2(String deliveryAddress2) {
        this.deliveryAddress2 = deliveryAddress2;
    }

    public String getDeliveryAddress3() {
        return deliveryAddress3;
    }

    public void setDeliveryAddress3(String deliveryAddress3) {
        this.deliveryAddress3 = deliveryAddress3;
    }

    public String getDeliveryAddress4() {
        return deliveryAddress4;
    }

    public void setDeliveryAddress4(String deliveryAddress4) {
        this.deliveryAddress4 = deliveryAddress4;
    }

    public String getSpecialInstructions1() {
        return specialInstructions1;
    }

    public void setSpecialInstructions1(String specialInstructions1) {
        this.specialInstructions1 = specialInstructions1;
    }

    public String getSpecialInstructions2() {
        return specialInstructions2;
    }

    public void setSpecialInstructions2(String specialInstructions2) {
        this.specialInstructions2 = specialInstructions2;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
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

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ManualInvoiceDTO manualInvoiceDTO = (ManualInvoiceDTO) o;
        if (manualInvoiceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), manualInvoiceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
