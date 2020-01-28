package uk.co.dryhome.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;
import uk.co.dryhome.domain.enumeration.InterestedType;
import uk.co.dryhome.domain.enumeration.CompanyType;
import uk.co.dryhome.domain.enumeration.LeadType;
import uk.co.dryhome.domain.enumeration.Status;

/**
 * A DTO for the Customer entity.
 */
public class CustomerDTO implements Serializable {

    private Long id;

    private String companyName;

    @NotNull
    @Size(min = 1, max = 100)
    private String address1;

    @Size(max = 100)
    private String address2;

    @Size(max = 100)
    private String address3;

    @NotNull
    @Size(min = 1, max = 50)
    private String town;

    @NotNull
    @Size(min = 1, max = 20)
    private String postCode;

    @Size(max = 100)
    private String title;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 20)
    private String tel;

    private String mobile;

    @Size(max = 50)
    private String email;

    private String products;

    private InterestedType interested;

    private BigDecimal paid;

    @NotNull
    private CompanyType type;

    @Lob
    private String notes;

    private LeadType lead;

    @Size(max = 100)
    private String leadName;

    @Size(max = 100)
    private String leadTel;

    @Size(max = 100)
    private String leadMob;

    private Status status;

    @Size(max = 100)
    private String enquiryProperty;

    @Size(max = 100)
    private String enquiryUnitPq;

    @Size(max = 100)
    private String enquiryInstPq;

    @Size(max = 100)
    private String saleProducts;

    @Size(max = 100)
    private String saleInvoiceDate;

    @Size(max = 100)
    private String saleInvoiceNumber;

    @Size(max = 100)
    private String saleInvoiceAmount;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public InterestedType getInterested() {
        return interested;
    }

    public void setInterested(InterestedType interested) {
        this.interested = interested;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public CompanyType getType() {
        return type;
    }

    public void setType(CompanyType type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LeadType getLead() {
        return lead;
    }

    public void setLead(LeadType lead) {
        this.lead = lead;
    }

    public String getLeadName() {
        return leadName;
    }

    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }

    public String getLeadTel() {
        return leadTel;
    }

    public void setLeadTel(String leadTel) {
        this.leadTel = leadTel;
    }

    public String getLeadMob() {
        return leadMob;
    }

    public void setLeadMob(String leadMob) {
        this.leadMob = leadMob;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getEnquiryProperty() {
        return enquiryProperty;
    }

    public void setEnquiryProperty(String enquiryProperty) {
        this.enquiryProperty = enquiryProperty;
    }

    public String getEnquiryUnitPq() {
        return enquiryUnitPq;
    }

    public void setEnquiryUnitPq(String enquiryUnitPq) {
        this.enquiryUnitPq = enquiryUnitPq;
    }

    public String getEnquiryInstPq() {
        return enquiryInstPq;
    }

    public void setEnquiryInstPq(String enquiryInstPq) {
        this.enquiryInstPq = enquiryInstPq;
    }

    public String getSaleProducts() {
        return saleProducts;
    }

    public void setSaleProducts(String saleProducts) {
        this.saleProducts = saleProducts;
    }

    public String getSaleInvoiceDate() {
        return saleInvoiceDate;
    }

    public void setSaleInvoiceDate(String saleInvoiceDate) {
        this.saleInvoiceDate = saleInvoiceDate;
    }

    public String getSaleInvoiceNumber() {
        return saleInvoiceNumber;
    }

    public void setSaleInvoiceNumber(String saleInvoiceNumber) {
        this.saleInvoiceNumber = saleInvoiceNumber;
    }

    public String getSaleInvoiceAmount() {
        return saleInvoiceAmount;
    }

    public void setSaleInvoiceAmount(String saleInvoiceAmount) {
        this.saleInvoiceAmount = saleInvoiceAmount;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + id +
            ", companyName='" + companyName + '\'' +
            ", address1='" + address1 + '\'' +
            ", address2='" + address2 + '\'' +
            ", address3='" + address3 + '\'' +
            ", town='" + town + '\'' +
            ", postCode='" + postCode + '\'' +
            ", title='" + title + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", tel='" + tel + '\'' +
            ", mobile='" + mobile + '\'' +
            ", email='" + email + '\'' +
            ", products='" + products + '\'' +
            ", interested=" + interested +
            ", paid=" + paid +
            ", type=" + type +
            ", notes='" + notes + '\'' +
            ", lead=" + lead +
            ", leadName='" + leadName + '\'' +
            ", leadTel='" + leadTel + '\'' +
            ", leadMob='" + leadMob + '\'' +
            ", status=" + status +
            ", enquiryProperty='" + enquiryProperty + '\'' +
            ", enquiryUnitPq='" + enquiryUnitPq + '\'' +
            ", enquiryInstPq='" + enquiryInstPq + '\'' +
            ", saleProducts='" + saleProducts + '\'' +
            ", saleInvoiceDate='" + saleInvoiceDate + '\'' +
            ", saleInvoiceNumber='" + saleInvoiceNumber + '\'' +
            ", saleInvoiceAmount='" + saleInvoiceAmount + '\'' +
            ", name='" + name + '\'' +
            '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (customerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
