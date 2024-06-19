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
        // todo add MDC
        log.info("Received request register new customer");

        try {
            Credentials credentials = customerService.register(customer);
            return new ResponseEntity<>(credentials, HttpStatus.OK);

        } catch (AbcException e) {
            log.info("Returning bad request. {}", e.getMessage());
            return new ResponseEntity<>(new AbcErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(new AbcErrorResponse("An unexpected error occured, for questions contact support@company.com"), HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            log.info("Finished request register new customer");

        }

    }

    @PostMapping(path = "logon")
    public ResponseEntity<Object> registerCustomer(@RequestBody Credentials credentials) {
        log.info("Received request logon customer");

        try {
            String token = customerService.logon(credentials);
            return new ResponseEntity<>(token, HttpStatus.OK);

        } catch (AbcException e) {
            log.info("Returning bad request. {}", e.getMessage());
            return new ResponseEntity<>(new AbcErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>(new AbcErrorResponse("An unexpected error occured, for questions contact support@company.com"), HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            log.info("Finished request logon customer");

        }

    }

}
