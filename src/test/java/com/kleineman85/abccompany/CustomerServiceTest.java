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
    void givenValidRequestShouldReturnCredentials() {
        // given
        Customer newCustomer = Customer.builder()
                .name("Joel")
                .address("My Street 100, New York")
                .dateOfBirth(LocalDate.of(1900, 1, 1))
                .idDocumentNumber("Passport12345")
                .username("kleineman85")
                .build();
        Customer mockedSavedCustomer = newCustomer;
        mockedSavedCustomer.setId(1);

        when(mockedCustomerRepository.findByUsername(newCustomer.getUsername())).thenReturn(Optional.empty());
        when(mockedCustomerRepository.save(newCustomer)).thenReturn(mockedSavedCustomer);

        // when
        Credentials result = testObject.registerCustomer(newCustomer);

        // then
        verify(mockedCustomerRepository, times(1)).findByUsername(any());
        verify(mockedCustomerRepository, times(1)).save(any());

        assertNotNull(mockedSavedCustomer.getPassword(), "password is not generated");
        // todo validate generated IBAN
        assertNotNull(mockedSavedCustomer.getIban(), "IBAN is not generated");

        assertEquals(mockedSavedCustomer.getUsername(), result.username(), "username does not match");
        assertEquals(mockedSavedCustomer.getPassword(), result.password(), "password does not match");
    }

    @Test
    void givenUserNameAlreadyExistsShouldThrowIllegalArgumentException() {
        // given
        Customer newCustomer = Customer.builder()
                .name("Joel")
                .address("My Street 100, New York")
                .dateOfBirth(LocalDate.of(1900, 1, 1))
                .idDocumentNumber("Passport12345")
                .username("kleineman85")
                .build();
        Customer existingCustomer = newCustomer;

        when(mockedCustomerRepository.findByUsername(newCustomer.getUsername())).thenReturn(Optional.of(existingCustomer));

        // when
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> testObject.registerCustomer(newCustomer));

        // then
        verify(mockedCustomerRepository, times(1)).findByUsername(any());
        verify(mockedCustomerRepository, times(0)).save(any());

        assertEquals("Invalid username: username already exists", result.getMessage());

    }


}