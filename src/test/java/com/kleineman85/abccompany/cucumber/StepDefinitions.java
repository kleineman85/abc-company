package com.kleineman85.abccompany.cucumber;

import com.kleineman85.abccompany.AbcErrorResponse;
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
    private boolean isValidRequest;

    ResponseEntity<Credentials> result;
    ResponseEntity<AbcErrorResponse> errorResult;

    @Given("{string} request")
    public void aValidRequest(String isValid) {
        // todo replace with json
        if (isValid.equals("a valid")) {
            isValidRequest = true;
        } else {
            isValidRequest = false;
        }
    }

    @And("the username {string} already exists")
    public void theUsernameAlreadyExists(String username) {
        customer = createCustomer(username);
        if (customerRepository.findByUsername(username).isEmpty()) {
            testRestTemplate.postForEntity(URL, customer, Credentials.class);
        }
    }

    @When("a client calls the register customer endpoint with username {string}")
    public void aClientCallsTheRegisterCustomerEndpoint(String username) {
        customer = createCustomer(username);

        if (isValidRequest) {
            result = testRestTemplate.postForEntity(URL, customer, Credentials.class);
        } else {
            errorResult = testRestTemplate.postForEntity(URL, customer, AbcErrorResponse.class);
        }
    }

    @Then("the client should receive {string} response {string}")
    public void theClientShouldReceiveSuccessfulResponseOK(String responseType, String httpStatusCode) {
        if (responseType.equals("successful")) {
            assertEquals(httpStatusCode, result.getStatusCode().toString());

        } else if (responseType.equals("error")) {
            assertEquals(httpStatusCode, errorResult.getStatusCode().toString());
        }
    }

    @And("the client should receive credentials")
    public void theClientShouldReceiveCredentials() {
        assertEquals(customer.getUsername(), result.getBody().username(), "username does not match");
        assertNotNull(result.getBody().password(), "password is not generated");
    }

    @And("the client should receive an AbcErrorResponse with error message {string}")
    public void theClientShouldReceiveAnAbcErrorResponse(String errorMessage) {
        assertEquals(errorMessage, errorResult.getBody().errorMessage());
    }

    @And("the customer api should create an entry in the database")
    public void theCustomerApiShouldCreateAnEntryInTheDatabase() {
        customerFromDatabase = customerRepository.findByUsername(customer.getUsername());
        assertTrue(customerFromDatabase.isPresent(), "entry is not created in database");
    }

    @And("the credentials should match the credentials in the database")
    public void theCredentialsShouldMatchTheCredentialsInTheDatabase() {
        assertEquals(result.getBody().password(), customerFromDatabase.get().getPassword(), "password does not match");
    }

    @And("the customer api should create an IBAN in the database")
    public void theCustomerApiShouldCreateAnIBANInTheDatabase() {
        assertNotNull(customerFromDatabase.get().getIban(), "IBAN is not created in database");
    }

    private Customer createCustomer(String username) {
        return Customer.builder()
                .name("John Doe")
                .address("Anonymous Street 100, New York")
                .dateOfBirth(LocalDate.of(1999, 1, 31))
                .idDocumentNumber("Passport12345")
                .username(username)
                .build();
    }

}
