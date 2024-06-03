package com.kleineman85.abccompany;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1", produces = "application/json")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping(path = "register")
    public ResponseEntity<Object> registerCustomer(@RequestParam(name = "name") String name,
                                                   @RequestParam(name = "address") String address,
                                                   @RequestParam(name = "dateofbirth") String dateOfBirth,
                                                   @RequestParam(name = "iddocumentnumber") String idDocumentNumber,
                                                   @RequestParam(name = "username") String username) {
        log.info("Received request register customer");

        try {
            Customer customer = Customer.builder()
                    .name(name)
                    .address(address)
                    // todo
                    .dateOfBirth(LocalDate.of(1900, 1, 1))
                    .idDocumentNumber(idDocumentNumber)
                    .username(username)
                    .build();

            Credentials credentials = customerService.registerCustomer(customer);

            return new ResponseEntity<>(credentials, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            log.info("Bad request: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Exception: ", e);
            return new ResponseEntity<>("An error occured, for questions contact support@company.com", HttpStatus.INTERNAL_SERVER_ERROR);

        } finally {
            log.info("Finished request and sending response");

        }

    }
}
