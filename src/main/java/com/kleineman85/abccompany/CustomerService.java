package com.kleineman85.abccompany;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.kleineman85.abccompany.AbcUtilities.generateDutchIban;
import static com.kleineman85.abccompany.AbcUtilities.generatePassword;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Credentials register(Customer newCustomer) {
        log.info("Start register new customer");
        validateUserName(newCustomer.getUsername());
        newCustomer.setPassword(generatePassword());
        newCustomer.setIban(generateDutchIban());

        Customer savedCustomer = customerRepository.save(newCustomer);
        log.info("Saved new customer to database");

        return new Credentials(savedCustomer.getUsername(), savedCustomer.getPassword());
    }

    public String logon(Credentials credentials) {
        log.info("Start logon customer");
        Optional<Customer> customer = customerRepository.findByUsername(credentials.username());

        if (customer.isPresent()) {
            String expectedPassword = getPassword(customer.get());
            validatePassword(expectedPassword, credentials.password());
        } else {
            log.info("Logon failed. Invalid username or password");
            throw new IllegalArgumentException("Invalid username or password");
        }
        log.info("Logon successful. Returning token");
        return "dummyTokenReplaceWithJwt";

    }

    private void validateUserName(String username) {
        // todo replace with predicate and custom exception
        if (customerRepository.findByUsername(username).isPresent()) {
            log.info("Register customer failed. Username already exists");
            throw new IllegalArgumentException("Invalid username: username already exists");
        }
    }

    private String getPassword(Customer customer) {
        if (customer.getPassword() != null) {
            return customer.getPassword();
        } else {
            log.error("Logon failed. Unexpected error");
            throw new RuntimeException("Unexpected error. Should never happen");
        }
    }

    private void validatePassword(String expectedPassword, String actualPassword) {
        if (!expectedPassword.equals(actualPassword)) {
            log.info("Logon failed. Invalid username or password");
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

}
