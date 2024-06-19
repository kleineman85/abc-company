package com.kleineman85.abccompany;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository mockedCustomerRepository;
    private CustomerService testObject;

    @BeforeEach
    void initialize() {
        testObject = new CustomerService(mockedCustomerRepository);
    }

    @Test
    void givenValidRequestWhenRegisteringShouldReturnCredentials() {
        // given
        Customer newCustomer = getCustomer();
        Customer mockedSavedCustomer = newCustomer;
        mockedSavedCustomer.setId(1);

        when(mockedCustomerRepository.findByUsername(newCustomer.getUsername())).thenReturn(Optional.empty());
        when(mockedCustomerRepository.save(newCustomer)).thenReturn(mockedSavedCustomer);

        // when
        Credentials result = testObject.register(newCustomer);

        // then
        verify(mockedCustomerRepository, times(1)).findByUsername(any());
        verify(mockedCustomerRepository, times(1)).save(any());

        assertNotNull(mockedSavedCustomer.getPassword(), "password is not generated");
        assertNotNull(mockedSavedCustomer.getIban(), "IBAN is not generated");
        assertEquals(mockedSavedCustomer.getUsername(), result.username(), "username does not match");
        assertEquals(mockedSavedCustomer.getPassword(), result.password(), "password does not match");
    }

    @Test
    void givenInvalidRequestUserNameAlreadyExistsWhenRegisteringShouldThrowIllegalArgumentException() {
        // given
        Customer newCustomer = getCustomer("John Doe Copy Cat", "kleineman85");
        Customer existingCustomer = getCustomer("John Doe", "kleineman85");

        when(mockedCustomerRepository.findByUsername(newCustomer.getUsername())).thenReturn(Optional.of(existingCustomer));

        // when
        AbcException result = assertThrows(AbcException.class, () -> testObject.register(newCustomer));

        // then
        verify(mockedCustomerRepository, times(1)).findByUsername(any());
        verify(mockedCustomerRepository, times(0)).save(any());

        assertEquals("Invalid username: username already exists", result.getMessage());
    }

    @Test
    void givenValidCredentialsWhenLoggingOnShouldReturnToken() {
        // given
        Credentials credentials = new Credentials("kleineman85", "Password1234");
        Customer mockedCustomer = getCustomerWithCredentials("kleineman85", "Password1234");

        when(mockedCustomerRepository.findByUsername(credentials.username())).thenReturn(Optional.of(mockedCustomer));

        // when
        String result = testObject.logon(credentials);

        // then
        assertNotNull(result);
        assertEquals("dummyTokenReplaceWithJwt", result);
    }

    @Test
    void givenInvalidUsernameWhenLoggingOnShouldThrowIllegalArgumentException() {
        // given
        Credentials credentials = new Credentials("kleineman85", "Password1234");

        when(mockedCustomerRepository.findByUsername(credentials.username())).thenReturn(Optional.empty());

        // when
        AbcException result = assertThrows(AbcException.class, () -> testObject.logon(credentials));

        // then
        verify(mockedCustomerRepository, times(1)).findByUsername(any());

        assertEquals("Invalid username or password", result.getMessage());
    }

    @Test
    void givenInvalidPasswordWhenLoggingOnShouldThrowIllegalArgumentException() {
        // given
        Credentials credentials = new Credentials("kleineman85", "Password4321");
        Customer mockedCustomer = getCustomerWithCredentials("kleineman85", "Password1234");

        when(mockedCustomerRepository.findByUsername(credentials.username())).thenReturn(Optional.of(mockedCustomer));

        // when
        AbcException result = assertThrows(AbcException.class, () -> testObject.logon(credentials));

        // then
        verify(mockedCustomerRepository, times(1)).findByUsername(any());

        assertEquals("Invalid username or password", result.getMessage());

    }

    @Test
    void givenCorruptedPasswordWhenLoggingOnShouldThrowRuntimeException() {
        // given
        Credentials credentials = new Credentials("kleineman85", "Password1234");
        Customer mockedCustomer = getCustomerWithCredentials("kleineman85", null);

        when(mockedCustomerRepository.findByUsername(credentials.username())).thenReturn(Optional.of(mockedCustomer));

        // when
        RuntimeException result = assertThrows(RuntimeException.class, () -> testObject.logon(credentials));

        // then
        verify(mockedCustomerRepository, times(1)).findByUsername(any());

        assertEquals("Unexpected error. No password in database for customer. Should never happen", result.getMessage());
    }

    private Customer getCustomer() {
        return getCustomer("John Doe", "kleineman85");
    }

    private Customer getCustomer(String name, String username) {
        return Customer.builder()
                .name(name)
                .address("Anonymous Street 100, New York")
                .dateOfBirth(LocalDate.of(1999, 1, 31))
                .idDocumentNumber("Passport12345")
                .username(username)
                .build();
    }

    private Customer getCustomerWithCredentials(String username, String password) {
        Customer customer = getCustomer();
        customer.setUsername(username);
        customer.setPassword(password);
        return customer;
    }

}