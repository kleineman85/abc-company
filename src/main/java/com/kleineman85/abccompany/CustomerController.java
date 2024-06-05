package com.kleineman85.abccompany;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1", consumes = "application/json", produces = "application/json")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping(path = "register")
    public ResponseEntity<Object> registerCustomer(@RequestBody Customer customer) {
        log.info("Received request register new customer");

        try {
            Credentials credentials = customerService.registerCustomer(customer);
            return new ResponseEntity<>(credentials, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            log.info("Bad request: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>("An unexpected error occured, for questions contact support@company.com", HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            log.info("Finished request register new customer");

        }

    }

    @PostMapping(path = "logon")
    public ResponseEntity<Object> registerCustomer(@RequestBody Credentials credentials) {
        log.info("Received request logon customer");

        try {
            customerService.logon(credentials);
            return new ResponseEntity<>("todo", HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            log.info("Bad request: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>("An unexpected error occured, for questions contact support@company.com", HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            log.info("Finished request logon customer");

        }

    }

}
