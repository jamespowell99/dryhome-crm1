package uk.co.dryhome.domain;


import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uk.co.dryhome.service.docs.DocTemplate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

/**
 * A ManualInvoice.
 */
@Entity
@Table(name = "manual_invoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ManualInvoice implements Serializable, MergeDocumentSource {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @NotNull
    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @NotNull
    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "jhi_ref")
    private String ref;

    @Column(name = "customer")
    private String customer;

    @Column(name = "address_1")
    private String address1;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "address_3")
    private String address3;

    @Column(name = "town")
    private String town;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "tel_no")
    private String telNo;

    @Column(name = "delivery_address_1")
    private String deliveryAddress1;

    @Column(name = "delivery_address_2")
    private String deliveryAddress2;

    @Column(name = "delivery_address_3")
    private String deliveryAddress3;

    @Column(name = "delivery_address_4")
    private String deliveryAddress4;

    @Column(name = "special_instructions_1")
    private String specialInstructions1;

    @Column(name = "special_instructions_2")
    private String specialInstructions2;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "payment_amount", precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @NotNull
    @Column(name = "vat_rate", precision = 10, scale = 2, nullable = false)
    private BigDecimal vatRate;

    @OneToMany(mappedBy = "manualInvoice")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OrderBy("id")
    private Set<ManualInvoiceItem> items = new HashSet<>();

    public BigDecimal getSubTotal() {
        return items.stream().map(ManualInvoiceItem::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getVatAmount() {
        //todo warning?
        return getSubTotal().multiply(getVatRate().divide(BigDecimal.valueOf(100))).setScale(2, BigDecimal.ROUND_HALF_UP);

    }

    public BigDecimal getTotal() {
        return getSubTotal().add(getVatAmount());
    }


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public ManualInvoice invoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public ManualInvoice orderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public ManualInvoice invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getRef() {
        return ref;
    }

    public ManualInvoice ref(String ref) {
        this.ref = ref;
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getCustomer() {
        return customer;
    }

    public ManualInvoice customer(String customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAddress1() {
        return address1;
    }

    public ManualInvoice address1(String address1) {
        this.address1 = address1;
        return this;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public ManualInvoice address2(String address2) {
        this.address2 = address2;
        return this;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public ManualInvoice address3(String address3) {
        this.address3 = address3;
        return this;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getTown() {
        return town;
    }

    public ManualInvoice town(String town) {
        this.town = town;
        return this;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostCode() {
        return postCode;
    }

    public ManualInvoice postCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getTelNo() {
        return telNo;
    }

    public ManualInvoice telNo(String telNo) {
        this.telNo = telNo;
        return this;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getDeliveryAddress1() {
        return deliveryAddress1;
    }

    public ManualInvoice deliveryAddress1(String deliveryAddress1) {
        this.deliveryAddress1 = deliveryAddress1;
        return this;
    }

    public void setDeliveryAddress1(String deliveryAddress1) {
        this.deliveryAddress1 = deliveryAddress1;
    }

    public String getDeliveryAddress2() {
        return deliveryAddress2;
    }

    public ManualInvoice deliveryAddress2(String deliveryAddress2) {
        this.deliveryAddress2 = deliveryAddress2;
        return this;
    }

    public void setDeliveryAddress2(String deliveryAddress2) {
        this.deliveryAddress2 = deliveryAddress2;
    }

    public String getDeliveryAddress3() {
        return deliveryAddress3;
    }

    public ManualInvoice deliveryAddress3(String deliveryAddress3) {
        this.deliveryAddress3 = deliveryAddress3;
        return this;
    }

    public void setDeliveryAddress3(String deliveryAddress3) {
        this.deliveryAddress3 = deliveryAddress3;
    }

    public String getDeliveryAddress4() {
        return deliveryAddress4;
    }

    public ManualInvoice deliveryAddress4(String deliveryAddress4) {
        this.deliveryAddress4 = deliveryAddress4;
        return this;
    }

    public void setDeliveryAddress4(String deliveryAddress4) {
        this.deliveryAddress4 = deliveryAddress4;
    }

    public String getSpecialInstructions1() {
        return specialInstructions1;
    }

    public ManualInvoice specialInstructions1(String specialInstructions1) {
        this.specialInstructions1 = specialInstructions1;
        return this;
    }

    public void setSpecialInstructions1(String specialInstructions1) {
        this.specialInstructions1 = specialInstructions1;
    }

    public String getSpecialInstructions2() {
        return specialInstructions2;
    }

    public ManualInvoice specialInstructions2(String specialInstructions2) {
        this.specialInstructions2 = specialInstructions2;
        return this;
    }

    public void setSpecialInstructions2(String specialInstructions2) {
        this.specialInstructions2 = specialInstructions2;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public ManualInvoice paymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public ManualInvoice paymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public ManualInvoice paymentType(String paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public ManualInvoice paymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public ManualInvoice vatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
        return this;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public Set<ManualInvoiceItem> getItems() {
        return items;
    }

    public ManualInvoice items(Set<ManualInvoiceItem> manualInvoiceItems) {
        this.items = manualInvoiceItems;
        return this;
    }

    public ManualInvoice addItems(ManualInvoiceItem manualInvoiceItem) {
        this.items.add(manualInvoiceItem);
        manualInvoiceItem.setManualInvoice(this);
        return this;
    }

    public ManualInvoice removeItems(ManualInvoiceItem manualInvoiceItem) {
        this.items.remove(manualInvoiceItem);
        manualInvoiceItem.setManualInvoice(null);
        return this;
    }

    public void setItems(Set<ManualInvoiceItem> manualInvoiceItems) {
        this.items = manualInvoiceItems;
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
        ManualInvoice manualInvoice = (ManualInvoice) o;
        if (manualInvoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), manualInvoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ManualInvoice{" +
            "id=" + getId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", orderNumber='" + getOrderNumber() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", ref='" + getRef() + "'" +
            ", customer='" + getCustomer() + "'" +
            ", address1='" + getAddress1() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", address3='" + getAddress3() + "'" +
            ", town='" + getTown() + "'" +
            ", postCode='" + getPostCode() + "'" +
            ", telNo='" + getTelNo() + "'" +
            ", deliveryAddress1='" + getDeliveryAddress1() + "'" +
            ", deliveryAddress2='" + getDeliveryAddress2() + "'" +
            ", deliveryAddress3='" + getDeliveryAddress3() + "'" +
            ", deliveryAddress4='" + getDeliveryAddress4() + "'" +
            ", specialInstructions1='" + getSpecialInstructions1() + "'" +
            ", specialInstructions2='" + getSpecialInstructions2() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", vatRate=" + getVatRate() +
            "}";
    }

    @Override
    public Map<String, String> documentMappings() {
        //todo overlap with CustomerOrder documentMappings?
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Map<String, String> map = new HashMap<>();
        map.put("invceNo", fieldToString(invoiceNumber));
        map.put("orderNo", fieldToString(orderNumber));
        map.put("invoiceDate", invoiceDate == null ? "" : invoiceDate.format(formatter));
        map.put("ref", fieldToString(ref));
        map.put("customerName", customer);
        List<String> address = Stream.of(address1, address2, address3, town, postCode)
            .filter(x -> !StringUtils.isEmpty(x))
            .collect(Collectors.toList());
        for (int i = 0; i < 5; i++) {
            try {
                map.put("address" + (i+1), address.get(i));
            } catch (IndexOutOfBoundsException e) {
                map.put("address" + (i+1), "");
            }
        }

        map.put("delAddress1", deliveryAddress1);
        map.put("delAddress2", deliveryAddress2);
        map.put("delAddress3", deliveryAddress3);
        map.put("delAddress4", deliveryAddress4);

        map.put("notes1", fieldToString(specialInstructions1));
        map.put("notes2", fieldToString(specialInstructions2));

        map.put("telNo", fieldToString(telNo));


        ArrayList<ManualInvoiceItem> orderedItems = new ArrayList<>(items);
        orderedItems.sort(Comparator.comparing(ManualInvoiceItem::getId));

        for (int i = 1; i <= 5; i++) {
            if (orderedItems.size() >= i) {
                ManualInvoiceItem item = orderedItems.get(i-1);
                map.put("item" + i, item.getProduct());
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


        if (paymentDate != null) {
            map.put("paymentDetailsHeader", "Payment Details");
            map.put("paymentStatus", paymentStatus);

            map.put("paymentTypeLabel", "Type:");
            map.put("paymentType", paymentType);
            map.put("paymentDateLabel", "Date:");
            map.put("paymentDate", paymentDate.format(formatter));
            map.put("paymentAmountLabel", "Amount");
            String invoicePaymentAmount;
            if (paymentAmount != null) {
                invoicePaymentAmount = "£" + paymentAmount.setScale(2, BigDecimal.ROUND_UP).toString();
            } else {
                invoicePaymentAmount = "";
            }
            map.put("paymentAmount", invoicePaymentAmount);
        } else {
            map.put("paymentDetailsHeader", "");
            map.put("paymentStatus", "");

            map.put("paymentTypeLabel", "");
            map.put("paymentType", "");
            map.put("paymentDateLabel", "");
            map.put("paymentDate", "");
            map.put("paymentAmountLabel", "");
            map.put("paymentAmount", "");
        }

        return map;
    }

    @Override
    public String getMergeDocPrefix(DocTemplate docTemplate) {
        return orderNumber + "-" + docTemplate.getTemplateName();
    }

    private String fieldToString(String field) {
        return firstNonNull(field, "");
    }

}
