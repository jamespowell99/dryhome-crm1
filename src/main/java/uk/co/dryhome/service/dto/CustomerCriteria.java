package uk.co.dryhome.service.dto;

import java.io.Serializable;
import java.util.Objects;
import uk.co.dryhome.domain.enumeration.CompanyType;
import uk.co.dryhome.domain.enumeration.InterestedType;
import uk.co.dryhome.domain.enumeration.LeadType;
import uk.co.dryhome.domain.enumeration.Status;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the Customer entity. This class is used in CustomerResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /customers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomerCriteria implements Serializable {
    /**
     * Class for filtering CompanyType
     */
    public static class CompanyTypeFilter extends Filter<CompanyType> {
    }
    /**
     * Class for filtering LeadType
     */
    public static class LeadTypeFilter extends Filter<LeadType> {
    }
    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {
    }
    /**
     * Class for filtering InterestedType
     */
    public static class InterestedTypeFilter extends Filter<InterestedType> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter companyName;

    private StringFilter address1;

    private StringFilter address2;

    private StringFilter address3;

    private StringFilter town;

    private StringFilter postCode;

    private StringFilter title;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter tel;

    private StringFilter mobile;

    private StringFilter email;

    private StringFilter products;

    private InterestedTypeFilter interested;

    private BigDecimalFilter paid;

    private CompanyTypeFilter type;

    private LeadTypeFilter lead;

    private StringFilter leadName;

    private StringFilter leadTel;

    private StringFilter leadMob;

    private StatusFilter status;

    private StringFilter enquiryProperty;

    private StringFilter enquiryUnitPq;

    private StringFilter enquiryInstPq;

    private StringFilter saleProducts;

    private StringFilter saleInvoiceDate;

    private StringFilter saleInvoiceNumber;

    private StringFilter saleInvoiceAmount;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCompanyName() {
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
    }

    public StringFilter getAddress1() {
        return address1;
    }

    public void setAddress1(StringFilter address1) {
        this.address1 = address1;
    }

    public StringFilter getAddress2() {
        return address2;
    }

    public void setAddress2(StringFilter address2) {
        this.address2 = address2;
    }

    public StringFilter getAddress3() {
        return address3;
    }

    public void setAddress3(StringFilter address3) {
        this.address3 = address3;
    }

    public StringFilter getTown() {
        return town;
    }

    public void setTown(StringFilter town) {
        this.town = town;
    }

    public StringFilter getPostCode() {
        return postCode;
    }

    public void setPostCode(StringFilter postCode) {
        this.postCode = postCode;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getTel() {
        return tel;
    }

    public void setTel(StringFilter tel) {
        this.tel = tel;
    }

    public StringFilter getMobile() {
        return mobile;
    }

    public void setMobile(StringFilter mobile) {
        this.mobile = mobile;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getProducts() {
        return products;
    }

    public void setProducts(StringFilter products) {
        this.products = products;
    }

    public InterestedTypeFilter getInterested() {
        return interested;
    }

    public void setInterested(InterestedTypeFilter interested) {
        this.interested = interested;
    }

    public BigDecimalFilter getPaid() {
        return paid;
    }

    public void setPaid(BigDecimalFilter paid) {
        this.paid = paid;
    }

    public CompanyTypeFilter getType() {
        return type;
    }

    public void setType(CompanyTypeFilter type) {
        this.type = type;
    }

    public LeadTypeFilter getLead() {
        return lead;
    }

    public void setLead(LeadTypeFilter lead) {
        this.lead = lead;
    }

    public StringFilter getLeadName() {
        return leadName;
    }

    public void setLeadName(StringFilter leadName) {
        this.leadName = leadName;
    }

    public StringFilter getLeadTel() {
        return leadTel;
    }

    public void setLeadTel(StringFilter leadTel) {
        this.leadTel = leadTel;
    }

    public StringFilter getLeadMob() {
        return leadMob;
    }

    public void setLeadMob(StringFilter leadMob) {
        this.leadMob = leadMob;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public StringFilter getEnquiryProperty() {
        return enquiryProperty;
    }

    public void setEnquiryProperty(StringFilter enquiryProperty) {
        this.enquiryProperty = enquiryProperty;
    }

    public StringFilter getEnquiryUnitPq() {
        return enquiryUnitPq;
    }

    public void setEnquiryUnitPq(StringFilter enquiryUnitPq) {
        this.enquiryUnitPq = enquiryUnitPq;
    }

    public StringFilter getEnquiryInstPq() {
        return enquiryInstPq;
    }

    public void setEnquiryInstPq(StringFilter enquiryInstPq) {
        this.enquiryInstPq = enquiryInstPq;
    }

    public StringFilter getSaleProducts() {
        return saleProducts;
    }

    public void setSaleProducts(StringFilter saleProducts) {
        this.saleProducts = saleProducts;
    }

    public StringFilter getSaleInvoiceDate() {
        return saleInvoiceDate;
    }

    public void setSaleInvoiceDate(StringFilter saleInvoiceDate) {
        this.saleInvoiceDate = saleInvoiceDate;
    }

    public StringFilter getSaleInvoiceNumber() {
        return saleInvoiceNumber;
    }

    public void setSaleInvoiceNumber(StringFilter saleInvoiceNumber) {
        this.saleInvoiceNumber = saleInvoiceNumber;
    }

    public StringFilter getSaleInvoiceAmount() {
        return saleInvoiceAmount;
    }

    public void setSaleInvoiceAmount(StringFilter saleInvoiceAmount) {
        this.saleInvoiceAmount = saleInvoiceAmount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerCriteria that = (CustomerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(address1, that.address1) &&
            Objects.equals(address2, that.address2) &&
            Objects.equals(address3, that.address3) &&
            Objects.equals(town, that.town) &&
            Objects.equals(postCode, that.postCode) &&
            Objects.equals(title, that.title) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(tel, that.tel) &&
            Objects.equals(mobile, that.mobile) &&
            Objects.equals(email, that.email) &&
            Objects.equals(products, that.products) &&
            Objects.equals(interested, that.interested) &&
            Objects.equals(paid, that.paid) &&
            Objects.equals(type, that.type) &&
            Objects.equals(lead, that.lead) &&
            Objects.equals(leadName, that.leadName) &&
            Objects.equals(leadTel, that.leadTel) &&
            Objects.equals(leadMob, that.leadMob) &&
            Objects.equals(status, that.status) &&
            Objects.equals(enquiryProperty, that.enquiryProperty) &&
            Objects.equals(enquiryUnitPq, that.enquiryUnitPq) &&
            Objects.equals(enquiryInstPq, that.enquiryInstPq) &&
            Objects.equals(saleProducts, that.saleProducts) &&
            Objects.equals(saleInvoiceDate, that.saleInvoiceDate) &&
            Objects.equals(saleInvoiceNumber, that.saleInvoiceNumber) &&
            Objects.equals(saleInvoiceAmount, that.saleInvoiceAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        companyName,
        address1,
        address2,
        address3,
        town,
        postCode,
        title,
        firstName,
        lastName,
        tel,
        mobile,
        email,
        products,
        interested,
        paid,
        type,
        lead,
        leadName,
        leadTel,
        leadMob,
        status,
        enquiryProperty,
        enquiryUnitPq,
        enquiryInstPq,
        saleProducts,
        saleInvoiceDate,
        saleInvoiceNumber,
        saleInvoiceAmount
        );
    }

    @Override
    public String toString() {
        return "CustomerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (companyName != null ? "companyName=" + companyName + ", " : "") +
                (address1 != null ? "address1=" + address1 + ", " : "") +
                (address2 != null ? "address2=" + address2 + ", " : "") +
                (address3 != null ? "address3=" + address3 + ", " : "") +
                (town != null ? "town=" + town + ", " : "") +
                (postCode != null ? "postCode=" + postCode + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (tel != null ? "tel=" + tel + ", " : "") +
                (mobile != null ? "mobile=" + mobile + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (products != null ? "products=" + products + ", " : "") +
                (interested != null ? "interested=" + interested + ", " : "") +
                (paid != null ? "paid=" + paid + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (lead != null ? "lead=" + lead + ", " : "") +
                (leadName != null ? "leadName=" + leadName + ", " : "") +
                (leadTel != null ? "leadTel=" + leadTel + ", " : "") +
                (leadMob != null ? "leadMob=" + leadMob + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (enquiryProperty != null ? "enquiryProperty=" + enquiryProperty + ", " : "") +
                (enquiryUnitPq != null ? "enquiryUnitPq=" + enquiryUnitPq + ", " : "") +
                (enquiryInstPq != null ? "enquiryInstPq=" + enquiryInstPq + ", " : "") +
                (saleProducts != null ? "saleProducts=" + saleProducts + ", " : "") +
                (saleInvoiceDate != null ? "saleInvoiceDate=" + saleInvoiceDate + ", " : "") +
                (saleInvoiceNumber != null ? "saleInvoiceNumber=" + saleInvoiceNumber + ", " : "") +
                (saleInvoiceAmount != null ? "saleInvoiceAmount=" + saleInvoiceAmount + ", " : "") +
            "}";
    }

}
