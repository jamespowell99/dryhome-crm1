package uk.co.dryhome;

import com.github.javafaker.Faker;
import com.google.gson.JsonParser;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.co.dryhome.domain.enumeration.CompanyType;
import uk.co.dryhome.service.dto.CustomerDTO;

import java.util.Collections;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Ignore
public class DataLoader {
    @Test
    public void test() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTU1NDY2NDQwM30.BCI3-EhZoH_b74V1oON8xv4e3nlSwC-kJk-FwfMB642fjEqnoZni6QnL8CPoPG2WFRYZTEooyZYQJrECWtj7Hg"));
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/api/authenticate", HttpMethod.POST, new HttpEntity<>("{\"username\":\"admin\",\"password\":\"admin\",\"rememberMe\":false}", headers), String.class);
        assert response.getStatusCode() == HttpStatus.OK;
        response.getBody();

        String accessToken = new JsonParser().parse(response.getBody()).getAsJsonObject().get("id_token").getAsString();

        System.out.println("accessToken = " + accessToken);

        for (int i = 0; i < 100; i++) {
            create(restTemplate, accessToken, createDp());
            create(restTemplate, accessToken, createDomestic());
        }
    }

    private void create(RestTemplate restTemplate, String accessToken, CustomerDTO customerToCreate) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + accessToken));
        ResponseEntity<Void> createCustomerResponse = restTemplate.exchange("http://localhost:8080/api/customers", HttpMethod.POST, new HttpEntity<>(customerToCreate, httpHeaders), Void.class);

        assertThat(createCustomerResponse.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    private CustomerDTO createDp() {
        Faker faker = new Faker(new Locale("en-GB"));

        CustomerDTO customerToCreate = new CustomerDTO();
        customerToCreate.setType(CompanyType.DAMP_PROOFER);
        customerToCreate.setCompanyName(faker.company().name());
        customerToCreate.setAddress1(faker.address().buildingNumber());
        customerToCreate.setAddress2(faker.address().secondaryAddress());
        customerToCreate.setAddress3(faker.address().streetAddress());
        customerToCreate.setTown(faker.address().city());
        customerToCreate.setPostCode(faker.address().zipCode());

        customerToCreate.setTitle(faker.name().prefix());
        customerToCreate.setFirstName(faker.name().firstName());
        customerToCreate.setLastName(faker.name().lastName());

        customerToCreate.setTel(faker.phoneNumber().phoneNumber());
        customerToCreate.setMobile(faker.phoneNumber().cellPhone());
        customerToCreate.setEmail(faker.internet().emailAddress());

        System.out.println("customerToCreate = " + customerToCreate);
        return customerToCreate;
    }

    private CustomerDTO createDomestic() {
        Faker faker = new Faker(new Locale("en-GB"));

        CustomerDTO customerToCreate = new CustomerDTO();
        customerToCreate.setType(CompanyType.DOMESTIC);
        customerToCreate.setCompanyName(null);
        customerToCreate.setAddress1(faker.address().buildingNumber());
        customerToCreate.setAddress2(faker.address().secondaryAddress());
        customerToCreate.setAddress3(faker.address().streetAddress());
        customerToCreate.setTown(faker.address().city());
        customerToCreate.setPostCode(faker.address().zipCode());

        customerToCreate.setTitle(faker.name().prefix());
        customerToCreate.setFirstName(faker.name().firstName());
        customerToCreate.setLastName(faker.name().lastName());

        customerToCreate.setTel(faker.phoneNumber().phoneNumber());
        customerToCreate.setMobile(faker.phoneNumber().cellPhone());
        customerToCreate.setEmail(faker.internet().emailAddress());

        System.out.println("customerToCreate = " + customerToCreate);
        return customerToCreate;
    }
}
