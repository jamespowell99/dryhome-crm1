package uk.co.dryhome.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import uk.co.dryhome.domain.enumeration.OrderMethod;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * A CustomerOrder.
 */
@Entity
@Table(name = "customer_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomerOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "order_number", length = 10, nullable = false)
    private String orderNumber;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Size(max = 100)
    @Column(name = "notes_1", length = 100)
    private String notes1;

    @Size(max = 100)
    @Column(name = "notes_2", length = 100)
    private String notes2;

    @Column(name = "despatch_date")
    private LocalDate despatchDate;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @NotNull
    @Column(name = "vat_rate", precision = 10, scale = 2, nullable = false)
    private BigDecimal vatRate;

    @Column(name = "internal_notes")
    private String internalNotes;

    @Size(max = 10)
    @Column(name = "invoice_number", length = 10)
    private String invoiceNumber;

    @Size(max = 100)
    @Column(name = "payment_status", length = 100)
    private String paymentStatus;

    @Size(max = 100)
    @Column(name = "payment_type", length = 100)
    private String paymentType;

    @Column(name = "payment_amount", precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @Size(max = 100)
    @Column(name = "placed_by", length = 100)
    private String placedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private OrderMethod method;

    @OneToMany(mappedBy = "customerOrder")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrderItem> items = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("customerOrders")
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public CustomerOrder orderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public CustomerOrder orderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getNotes1() {
        return notes1;
    }

    public CustomerOrder notes1(String notes1) {
        this.notes1 = notes1;
        return this;
    }

    public void setNotes1(String notes1) {
        this.notes1 = notes1;
    }

    public String getNotes2() {
        return notes2;
    }

    public CustomerOrder notes2(String notes2) {
        this.notes2 = notes2;
        return this;
    }

    public void setNotes2(String notes2) {
        this.notes2 = notes2;
    }

    public LocalDate getDespatchDate() {
        return despatchDate;
    }

    public CustomerOrder despatchDate(LocalDate despatchDate) {
        this.despatchDate = despatchDate;
        return this;
    }

    public void setDespatchDate(LocalDate despatchDate) {
        this.despatchDate = despatchDate;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public CustomerOrder invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public CustomerOrder paymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public CustomerOrder vatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
        return this;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public CustomerOrder internalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
        return this;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public CustomerOrder invoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public CustomerOrder paymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public CustomerOrder paymentType(String paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public CustomerOrder paymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPlacedBy() {
        return placedBy;
    }

    public CustomerOrder placedBy(String placedBy) {
        this.placedBy = placedBy;
        return this;
    }

    public void setPlacedBy(String placedBy) {
        this.placedBy = placedBy;
    }

    public OrderMethod getMethod() {
        return method;
    }

    public CustomerOrder method(OrderMethod method) {
        this.method = method;
        return this;
    }

    public void setMethod(OrderMethod method) {
        this.method = method;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public CustomerOrder items(Set<OrderItem> orderItems) {
        this.items = orderItems;
        return this;
    }

    public CustomerOrder addItems(OrderItem orderItem) {
        this.items.add(orderItem);
        orderItem.setCustomerOrder(this);
        return this;
    }

    public CustomerOrder removeItems(OrderItem orderItem) {
        this.items.remove(orderItem);
        orderItem.setCustomerOrder(null);
        return this;
    }

    public void setItems(Set<OrderItem> orderItems) {
        this.items = orderItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public CustomerOrder customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomerOrder customerOrder = (CustomerOrder) o;
        if (customerOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
            "id=" + getId() +
            ", orderNumber='" + getOrderNumber() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", notes1='" + getNotes1() + "'" +
            ", notes2='" + getNotes2() + "'" +
            ", despatchDate='" + getDespatchDate() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", vatRate=" + getVatRate() +
            ", internalNotes='" + getInternalNotes() + "'" +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", placedBy='" + getPlacedBy() + "'" +
            ", method='" + getMethod() + "'" +
            "}";
    }

    public BigDecimal getSubTotal() {
        return items.stream().map(OrderItem::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getVatAmount() {
        return getSubTotal().multiply(getVatRate().divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP));

    }

    public BigDecimal getTotal() {
        return getSubTotal().add(getVatAmount());
    }

    private String fieldToString(String field) {
        return firstNonNull(field, "");
    }

    public Map<String, String> documentMappings() {
        Map<String, String> map = new HashMap<>();
        map.put("invceNo", fieldToString(invoiceNumber));
        map.put("orderNo", fieldToString(orderNumber));
//        SimpleDateFormat dateFormatterDate = new SimpleDateFormat("dd/MM/yyyy");
//        map.put("invoiceDate", dateFormatterDate.format(invoiceDate));
        map.put("invoiceDate", invoiceDate == null ? "" : invoiceDate.toString()); //todo format properly
        map.put("ref", fieldToString(placedBy));
        map.put("customerName", customer.getName());
        List<String> address = Stream.of(customer.getAddress1(), customer.getAddress2(), customer.getAddress3(), customer.getTown(), customer.getPostCode())
            .filter(x -> !StringUtils.isEmpty(x))
            .collect(Collectors.toList());
        for (int i = 0; i < 5; i++) {
            try {
                map.put("address" + (i+1), address.get(i));
            } catch (IndexOutOfBoundsException e) {
                map.put("address" + (i+1), "");
            }
        }

        map.put("delAddress1", map.get("address1"));
        map.put("delAddress2", map.get("address2"));
        map.put("delAddress3", map.get("address3"));
        map.put("delAddress4", map.get("address4"));

        map.put("notes1", fieldToString(notes1));
        map.put("notes2", fieldToString(notes2));

        map.put("telNo", firstNonNull(customer.getTel(), customer.getMobile(), ""));


        ArrayList<OrderItem> orderedItems = new ArrayList<>(items);
        orderedItems.sort(Comparator.comparing(OrderItem::getId));

        for (int i = 1; i <= 5; i++) {
            if (orderedItems.size() >= i) {
            OrderItem item = orderedItems.get(i-1);
                map.put("item" + i, item.getProduct().getName());
                map.put("qt" + i, item.getQuantity().toString());
                map.put("price" + i, item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                map.put("total" + i, item.getSubTotal().setScale(2, BigDecimal.ROUND_UP).toString());
            } else {
                map.put("item" + i, "");
                map.put("qt" + i, "");
                map.put("price" + i, "");
                map.put("total" + i, "");
            }
        }

        map.put("subtl", getSubTotal().setScale(2, BigDecimal.ROUND_UP).toString());
        map.put("vat", getVatAmount().setScale(2, BigDecimal.ROUND_UP).toString());
        map.put("total", getTotal().setScale(2, BigDecimal.ROUND_UP).toString());
        map.put("vrt", new DecimalFormat("#0.#").format(vatRate));


        //todo complete
        map.put("paymentDetailsHeader", "");
        map.put("paymentStatus", "");

        map.put("paymentTypeLabel", "");
        map.put("paymentType", "");
        map.put("paymentDateLabel", "");
        map.put("paymentDate", "");
        map.put("paymentAmountLabel", "");
        map.put("paymentAmount", "");

        return map;
    }
}
