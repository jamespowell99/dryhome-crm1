package uk.co.dryhome.domain;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import uk.co.dryhome.domain.enumeration.CompanyType;
import uk.co.dryhome.domain.enumeration.InterestedType;
import uk.co.dryhome.domain.enumeration.LeadType;
import uk.co.dryhome.domain.enumeration.Status;
import uk.co.dryhome.service.docs.DocTemplate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Slf4j
public class Customer implements Serializable, MergeDocumentSource {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "address_1", length = 100, nullable = false)
    private String address1;

    @Size(max = 100)
    @Column(name = "address_2", length = 100)
    private String address2;

    @Size(max = 100)
    @Column(name = "address_3", length = 100)
    private String address3;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "town", length = 50, nullable = false)
    private String town;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "post_code", length = 20, nullable = false)
    private String postCode;

    @Size(max = 100)
    @Column(name = "title", length = 100)
    private String title;

    @Size(max = 100)
    @Column(name = "first_name", length = 100)
    private String firstName;

    @Size(max = 100)
    @Column(name = "last_name", length = 100)
    private String lastName;

    @Size(max = 20)
    @Column(name = "tel", length = 20)
    private String tel;

    @Column(name = "mobile")
    private String mobile;

    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "products")
    private String products;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_interested")
    private InterestedType interested;

    @Column(name = "paid", precision = 10, scale = 2)
    private BigDecimal paid;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private CompanyType type;

    @Lob
    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_lead")
    private LeadType lead;

    @Size(max = 100)
    @Column(name = "lead_name", length = 100)
    private String leadName;

    @Size(max = 100)
    @Column(name = "lead_tel", length = 100)
    private String leadTel;

    @Size(max = 100)
    @Column(name = "lead_mob", length = 100)
    private String leadMob;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Size(max = 100)
    @Column(name = "enquiry_property", length = 100)
    private String enquiryProperty;

    @Size(max = 100)
    @Column(name = "enquiry_unit_pq", length = 100)
    private String enquiryUnitPq;

    @Size(max = 100)
    @Column(name = "enquiry_inst_pq", length = 100)
    private String enquiryInstPq;

    @Size(max = 100)
    @Column(name = "sale_products", length = 100)
    private String saleProducts;

    @Size(max = 100)
    @Column(name = "sale_invoice_date", length = 100)
    private String saleInvoiceDate;

    @Size(max = 100)
    @Column(name = "sale_invoice_number", length = 100)
    private String saleInvoiceNumber;

    @Size(max = 100)
    @Column(name = "sale_invoice_amount", length = 100)
    private String saleInvoiceAmount;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Customer companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress1() {
        return address1;
    }

    public Customer address1(String address1) {
        this.address1 = address1;
        return this;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public Customer address2(String address2) {
        this.address2 = address2;
        return this;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public Customer address3(String address3) {
        this.address3 = address3;
        return this;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getTown() {
        return town;
    }

    public Customer town(String town) {
        this.town = town;
        return this;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostCode() {
        return postCode;
    }

    public Customer postCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getTitle() {
        return title;
    }

    public Customer title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public Customer firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Customer lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTel() {
        return tel;
    }

    public Customer tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public Customer mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public Customer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProducts() {
        return products;
    }

    public Customer products(String products) {
        this.products = products;
        return this;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public InterestedType getInterested() {
        return interested;
    }

    public Customer interested(InterestedType interested) {
        this.interested = interested;
        return this;
    }

    public void setInterested(InterestedType interested) {
        this.interested = interested;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public Customer paid(BigDecimal paid) {
        this.paid = paid;
        return this;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public CompanyType getType() {
        return type;
    }

    public Customer type(CompanyType type) {
        this.type = type;
        return this;
    }

    public void setType(CompanyType type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public Customer notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LeadType getLead() {
        return lead;
    }

    public Customer lead(LeadType lead) {
        this.lead = lead;
        return this;
    }

    public void setLead(LeadType lead) {
        this.lead = lead;
    }

    public String getLeadName() {
        return leadName;
    }

    public Customer leadName(String leadName) {
        this.leadName = leadName;
        return this;
    }

    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }

    public String getLeadTel() {
        return leadTel;
    }

    public Customer leadTel(String leadTel) {
        this.leadTel = leadTel;
        return this;
    }

    public void setLeadTel(String leadTel) {
        this.leadTel = leadTel;
    }

    public String getLeadMob() {
        return leadMob;
    }

    public Customer leadMob(String leadMob) {
        this.leadMob = leadMob;
        return this;
    }

    public void setLeadMob(String leadMob) {
        this.leadMob = leadMob;
    }

    public Status getStatus() {
        return status;
    }

    public Customer status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getEnquiryProperty() {
        return enquiryProperty;
    }

    public Customer enquiryProperty(String enquiryProperty) {
        this.enquiryProperty = enquiryProperty;
        return this;
    }

    public void setEnquiryProperty(String enquiryProperty) {
        this.enquiryProperty = enquiryProperty;
    }

    public String getEnquiryUnitPq() {
        return enquiryUnitPq;
    }

    public Customer enquiryUnitPq(String enquiryUnitPq) {
        this.enquiryUnitPq = enquiryUnitPq;
        return this;
    }

    public void setEnquiryUnitPq(String enquiryUnitPq) {
        this.enquiryUnitPq = enquiryUnitPq;
    }

    public String getEnquiryInstPq() {
        return enquiryInstPq;
    }

    public Customer enquiryInstPq(String enquiryInstPq) {
        this.enquiryInstPq = enquiryInstPq;
        return this;
    }

    public void setEnquiryInstPq(String enquiryInstPq) {
        this.enquiryInstPq = enquiryInstPq;
    }

    public String getSaleProducts() {
        return saleProducts;
    }

    public Customer saleProducts(String saleProducts) {
        this.saleProducts = saleProducts;
        return this;
    }

    public void setSaleProducts(String saleProducts) {
        this.saleProducts = saleProducts;
    }

    public String getSaleInvoiceDate() {
        return saleInvoiceDate;
    }

    public Customer saleInvoiceDate(String saleInvoiceDate) {
        this.saleInvoiceDate = saleInvoiceDate;
        return this;
    }

    public void setSaleInvoiceDate(String saleInvoiceDate) {
        this.saleInvoiceDate = saleInvoiceDate;
    }

    public String getSaleInvoiceNumber() {
        return saleInvoiceNumber;
    }

    public Customer saleInvoiceNumber(String saleInvoiceNumber) {
        this.saleInvoiceNumber = saleInvoiceNumber;
        return this;
    }

    public void setSaleInvoiceNumber(String saleInvoiceNumber) {
        this.saleInvoiceNumber = saleInvoiceNumber;
    }

    public String getSaleInvoiceAmount() {
        return saleInvoiceAmount;
    }

    public Customer saleInvoiceAmount(String saleInvoiceAmount) {
        this.saleInvoiceAmount = saleInvoiceAmount;
        return this;
    }

    public void setSaleInvoiceAmount(String saleInvoiceAmount) {
        this.saleInvoiceAmount = saleInvoiceAmount;
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
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", address1='" + getAddress1() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", address3='" + getAddress3() + "'" +
            ", town='" + getTown() + "'" +
            ", postCode='" + getPostCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", tel='" + getTel() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", email='" + getEmail() + "'" +
            ", products='" + getProducts() + "'" +
            ", interested='" + getInterested() + "'" +
            ", paid=" + getPaid() +
            ", type='" + getType() + "'" +
            ", notes='" + getNotes() + "'" +
            ", lead='" + getLead() + "'" +
            ", leadName='" + getLeadName() + "'" +
            ", leadTel='" + getLeadTel() + "'" +
            ", leadMob='" + getLeadMob() + "'" +
            ", status='" + getStatus() + "'" +
            ", enquiryProperty='" + getEnquiryProperty() + "'" +
            ", enquiryUnitPq='" + getEnquiryUnitPq() + "'" +
            ", enquiryInstPq='" + getEnquiryInstPq() + "'" +
            ", saleProducts='" + getSaleProducts() + "'" +
            ", saleInvoiceDate='" + getSaleInvoiceDate() + "'" +
            ", saleInvoiceNumber='" + getSaleInvoiceNumber() + "'" +
            ", saleInvoiceAmount='" + getSaleInvoiceAmount() + "'" +
            "}";
    }

    @Override
    public Map<String, String> documentMappings() {
        Map<String, String> map = new HashMap<>();
        map.put("companyName", companyName);
        map.put("companyId", Long.toString(id));
        map.put("customerId", Long.toString(id));
        List<String> address = Stream.of(address1, address2, address3, town, postCode)
            .filter(x -> !StringUtils.isEmpty(x))
            .collect(Collectors.toList());
        map.put("tel", tel);
        map.put("mob", mobile);
        String contact = title + " " + firstName + " " + lastName;
        map.put("contact", contact);
        map.put("paid", paid == null ? "" : new DecimalFormat("#0.##").format(paid));
        map.put("products", products);
        map.put("notes", notes);

        map.put("firstName", firstName);

        for (int i = 0; i < 5; i++) {
            try {
                map.put("address" + (i+1), address.get(i));
            } catch (IndexOutOfBoundsException e) {
                map.put("address" + (i+1), "");
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(contact);
        for (int i = contact.length(); i < 100; i++) {
            sb.append(" ");
        }
        Date now = new Date();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
        String dateAsString = dateFormatter.format(now);
        sb.append(dateAsString);

        SimpleDateFormat dateFormatterDate = new SimpleDateFormat("dd/MM/yyyy");
        map.put("currentDate", dateFormatterDate.format(now));

        SimpleDateFormat dateFormatterTime = new SimpleDateFormat("hh:mm:ss");
        map.put("currentTime", dateFormatterTime.format(now));


        map.put("contactLine", sb.toString());

        map.put("currentDate", dateAsString);

        map.put("lead", lead == null ? null : lead.name());
        map.put("leadName", leadName);
        map.put("leadTel", leadTel);
        map.put("leadMob", leadMob);

        map.put("enquiryProperty", enquiryProperty);
        map.put("enquiryUnitPq", enquiryUnitPq);
        map.put("enquiryInstPq", enquiryInstPq);

        map.put("saleProducts", saleProducts);
        map.put("saleInvoiceDate", saleInvoiceDate);
        map.put("saleInvoiceNumber", saleInvoiceNumber);
        map.put("saleInvoiceAmount", saleInvoiceAmount);

        List<String> labelsContent = Stream.of(contact, companyName, address1, address2, address3, town, postCode)
            .filter(x -> !StringUtils.isEmpty(x))
            .collect(Collectors.toList());

        for (int i = 0; i < 8; i++) {
            for (int n = 0; n < 7; n++) {

                String key = "label" + (i + 1) + "Line" + (n + 1);
                try {
                    map.put(key, labelsContent.get(n));
                } catch (IndexOutOfBoundsException e) {
                    map.put(key, "");
                }
                log.debug(key + ": " + map.get(key));
            }
        }

        map.put("fullLetterAddress", getFullLetterAddress());

        return map;
    }

    @Override
    public String getMergeDocPrefix(DocTemplate docTemplate) {
        return getName() + "-" + docTemplate.getTemplateName();
    }

    public String getName() {
        if (type == CompanyType.DOMESTIC) {
            return firstName + " " + lastName;
        } else {
            return companyName;
        }
    }

    public String getFullContactName() {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(title)) {
            sb.append(title);
            sb.append(" ");
        }

        if (!StringUtils.isEmpty(firstName)) {
            sb.append(firstName);
            sb.append(" ");
        }

        if (!StringUtils.isEmpty(lastName)) {
            sb.append(lastName);
        }

        return sb.toString();
    }

    public String getFullLetterAddress() {
        ArrayList<String> allFields = new ArrayList<>();
        allFields.add(getFullContactName());
        allFields.add(companyName);
        allFields.add(address1);
        allFields.add(address2);
        allFields.add(address3);
        allFields.add(town);
        allFields.add(postCode);

        List<String> blankLinesRemoved = allFields.stream()
            .filter(StringUtils::isNotEmpty)
            .collect(Collectors.toList());

        List<String> fullLetterAddress = new ArrayList<>();

        for (int i = 0; i < (7 - blankLinesRemoved.size()); i ++) {
            fullLetterAddress.add("");
        }
        fullLetterAddress.addAll(blankLinesRemoved);

        return fullLetterAddress.stream().collect(Collectors.joining("\n"));
    }
}
