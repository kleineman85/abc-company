package com.kleineman85.abccompany;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.kleineman85.abccompany.AbcUtilities.generateDutchIban;
import static com.kleineman85.abccompany.AbcUtilities.generatePassword;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Credentials registerCustomer(Customer newCustomer) {
        log.info("Start register customer");
        // validations
        validateUserName(newCustomer.getUsername());

        // enrichment's
        newCustomer.setPassword(generatePassword());
        newCustomer.setIban(generateDutchIban());

        // if valid save
        Customer savedCustomer = customerRepository.save(newCustomer);

        // return credentials
        return new Credentials(savedCustomer.getUsername(), savedCustomer.getPassword());

    }

    private void validateUserName(String username) {
        // todo replace with predicate and custom exception
        if (customerRepository.findByUsername(username).isPresent())
            throw new IllegalArgumentException("Invalid username: username already exists");
    }

}
