package uk.co.dryhome.service.dto;

import java.io.Serializable;
import java.util.Objects;
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

    private StringFilter interested;

    private BigDecimalFilter paid;

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

    public StringFilter getInterested() {
        return interested;
    }

    public void setInterested(StringFilter interested) {
        this.interested = interested;
    }

    public BigDecimalFilter getPaid() {
        return paid;
    }

    public void setPaid(BigDecimalFilter paid) {
        this.paid = paid;
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
            Objects.equals(paid, that.paid);
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
        paid
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
            "}";
    }

}
