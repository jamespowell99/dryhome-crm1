package uk.co.dryhome.domain;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class CustomerTest {

    private Customer customer = new Customer();

    @Before
    public void setup() {
        customer.setTitle("Mr");
        customer.setFirstName("James");
        customer.setLastName("Powell");
        customer.setCompanyName("Powtech");
        customer.setAddress1("Wentworth House");
        customer.setAddress2("School Lane");
        customer.setAddress3("Bushey");
        customer.setTown("WATFORD");
        customer.setPostCode("WD23 1SS");
    }

    @Test
    public void testNoFields() {
        customer.setTitle(null);
        customer.setFirstName(null);
        customer.setLastName(null);
        customer.setCompanyName(null);
        customer.setAddress1(null);
        customer.setAddress2(null);
        customer.setAddress3(null);
        customer.setTown(null);
        customer.setPostCode(null);
        assertThat(customer.getFullLetterAddress(), equalTo("\n\n\n\n\n\n"));
    }

    @Test
    public void testCompanyFull() {
        assertThat(customer.getFullLetterAddress(), equalTo("Mr James Powell\nPowtech\nWentworth House\nSchool Lane\nBushey\nWATFORD\nWD23 1SS"));
    }

    @Test
    public void testNoCompanyName() {
        customer.setCompanyName(null);
        assertThat(customer.getFullLetterAddress(), equalTo("\nMr James Powell\nWentworth House\nSchool Lane\nBushey\nWATFORD\nWD23 1SS"));
    }

    @Test
    public void testMinimalAddressNoCompanyName() {
        customer.setCompanyName(null);
        customer.setAddress2(null);
        customer.setAddress3(null);
        assertThat(customer.getFullLetterAddress(), equalTo("\n\n\nMr James Powell\nWentworth House\nWATFORD\nWD23 1SS"));
    }

    @Test
    public void testMinimalAddressWithCompanyName() {
        customer.setAddress2(null);
        customer.setAddress3(null);
        assertThat(customer.getFullLetterAddress(), equalTo("\n\nMr James Powell\nPowtech\nWentworth House\nWATFORD\nWD23 1SS"));
    }
}
