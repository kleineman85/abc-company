package com.kleineman85.abccompany.cucumber;

import com.kleineman85.abccompany.Credentials;
import com.kleineman85.abccompany.Customer;
import com.kleineman85.abccompany.CustomerRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class StepDefinitions extends CucumberTest {
    private final String URL = "/api/v1/register";
    @Autowired
    private final TestRestTemplate testRestTemplate;
    @Autowired
    private final CustomerRepository customerRepository;

    private Customer customer;
    private Optional<Customer> customerFromDatabase;

    ResponseEntity<Credentials> result;

    @Given("a valid request")
    public void aValidRequest() {
        // todo replace with json
        customer = Customer.builder()
                .name("John Doe")
                .address("Anonymous Street 100, New York")
                .dateOfBirth(LocalDate.of(1999, 1, 31))
                .idDocumentNumber("Passport12345")
                .username("kleineman85")
                .build();
    }

    @When("a client calls the register customer endpoint")
    public void aClientCallsTheRegisterCustomerEndpoint() {
        result = testRestTemplate.postForEntity(URL, customer, Credentials.class);
    }

    @Then("the client should receive credentials")
    public void theClientShouldReceiveCredentials() {
        assertEquals(customer.getUsername(), result.getBody().username());
        assertNotNull(result.getBody().password(), "password is not generated");
    }

    @And("the customer api should create an entry in the database")
    public void theCustomerApiShouldCreateAnEntryInTheDatabase() {
        customerFromDatabase = customerRepository.findByUsername(customer.getUsername());
        assertTrue(customerFromDatabase.isPresent());
    }

    @And("the credentials should match the credentials in the database")
    public void theCredentialsShouldMatchTheCredentialsInTheDatabase() {
        assertEquals(result.getBody().password(), customerFromDatabase.get().getPassword());
    }

    @And("the customer api should create an IBAN in the database")
    public void theCustomerApiShouldCreateAnIBANInTheDatabase() {
        assertNotNull(customerFromDatabase.get().getIban());
    }

}
