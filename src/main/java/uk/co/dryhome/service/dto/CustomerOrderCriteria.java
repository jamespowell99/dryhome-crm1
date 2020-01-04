package uk.co.dryhome.service.dto;

import java.io.Serializable;
import java.util.Objects;
import uk.co.dryhome.domain.enumeration.OrderMethod;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the CustomerOrder entity. This class is used in CustomerOrderResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /customer-orders?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomerOrderCriteria implements Serializable {
    /**
     * Class for filtering OrderMethod
     */
    public static class OrderMethodFilter extends Filter<OrderMethod> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter orderNumber;

    private LocalDateFilter orderDate;

    private StringFilter notes1;

    private StringFilter notes2;

    private LocalDateFilter despatchDate;

    private LocalDateFilter invoiceDate;

    private LocalDateFilter paymentDate;

    private BigDecimalFilter vatRate;

    private StringFilter internalNotes;

    private StringFilter invoiceNumber;

    private StringFilter paymentStatus;

    private StringFilter paymentType;

    private BigDecimalFilter paymentAmount;

    private StringFilter placedBy;

    private OrderMethodFilter method;

    private LongFilter itemsId;

    private LongFilter customerId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(StringFilter orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDateFilter getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateFilter orderDate) {
        this.orderDate = orderDate;
    }

    public StringFilter getNotes1() {
        return notes1;
    }

    public void setNotes1(StringFilter notes1) {
        this.notes1 = notes1;
    }

    public StringFilter getNotes2() {
        return notes2;
    }

    public void setNotes2(StringFilter notes2) {
        this.notes2 = notes2;
    }

    public LocalDateFilter getDespatchDate() {
        return despatchDate;
    }

    public void setDespatchDate(LocalDateFilter despatchDate) {
        this.despatchDate = despatchDate;
    }

    public LocalDateFilter getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateFilter invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDateFilter getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateFilter paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimalFilter getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimalFilter vatRate) {
        this.vatRate = vatRate;
    }

    public StringFilter getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(StringFilter internalNotes) {
        this.internalNotes = internalNotes;
    }

    public StringFilter getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(StringFilter invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public StringFilter getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(StringFilter paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public StringFilter getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(StringFilter paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimalFilter getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimalFilter paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public StringFilter getPlacedBy() {
        return placedBy;
    }

    public void setPlacedBy(StringFilter placedBy) {
        this.placedBy = placedBy;
    }

    public OrderMethodFilter getMethod() {
        return method;
    }

    public void setMethod(OrderMethodFilter method) {
        this.method = method;
    }

    public LongFilter getItemsId() {
        return itemsId;
    }

    public void setItemsId(LongFilter itemsId) {
        this.itemsId = itemsId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerOrderCriteria that = (CustomerOrderCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(orderNumber, that.orderNumber) &&
            Objects.equals(orderDate, that.orderDate) &&
            Objects.equals(notes1, that.notes1) &&
            Objects.equals(notes2, that.notes2) &&
            Objects.equals(despatchDate, that.despatchDate) &&
            Objects.equals(invoiceDate, that.invoiceDate) &&
            Objects.equals(paymentDate, that.paymentDate) &&
            Objects.equals(vatRate, that.vatRate) &&
            Objects.equals(internalNotes, that.internalNotes) &&
            Objects.equals(invoiceNumber, that.invoiceNumber) &&
            Objects.equals(paymentStatus, that.paymentStatus) &&
            Objects.equals(paymentType, that.paymentType) &&
            Objects.equals(paymentAmount, that.paymentAmount) &&
            Objects.equals(placedBy, that.placedBy) &&
            Objects.equals(method, that.method) &&
            Objects.equals(itemsId, that.itemsId) &&
            Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        orderNumber,
        orderDate,
        notes1,
        notes2,
        despatchDate,
        invoiceDate,
        paymentDate,
        vatRate,
        internalNotes,
        invoiceNumber,
        paymentStatus,
        paymentType,
        paymentAmount,
        placedBy,
        method,
        itemsId,
        customerId
        );
    }

    @Override
    public String toString() {
        return "CustomerOrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (orderNumber != null ? "orderNumber=" + orderNumber + ", " : "") +
                (orderDate != null ? "orderDate=" + orderDate + ", " : "") +
                (notes1 != null ? "notes1=" + notes1 + ", " : "") +
                (notes2 != null ? "notes2=" + notes2 + ", " : "") +
                (despatchDate != null ? "despatchDate=" + despatchDate + ", " : "") +
                (invoiceDate != null ? "invoiceDate=" + invoiceDate + ", " : "") +
                (paymentDate != null ? "paymentDate=" + paymentDate + ", " : "") +
                (vatRate != null ? "vatRate=" + vatRate + ", " : "") +
                (internalNotes != null ? "internalNotes=" + internalNotes + ", " : "") +
                (invoiceNumber != null ? "invoiceNumber=" + invoiceNumber + ", " : "") +
                (paymentStatus != null ? "paymentStatus=" + paymentStatus + ", " : "") +
                (paymentType != null ? "paymentType=" + paymentType + ", " : "") +
                (paymentAmount != null ? "paymentAmount=" + paymentAmount + ", " : "") +
                (placedBy != null ? "placedBy=" + placedBy + ", " : "") +
                (method != null ? "method=" + method + ", " : "") +
                (itemsId != null ? "itemsId=" + itemsId + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
            "}";
    }

}
