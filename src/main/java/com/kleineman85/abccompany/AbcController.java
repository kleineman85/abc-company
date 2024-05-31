package com.kleineman85.abccompany;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1")
public class AbcController {
    private final AbcService abcService;

    @PostMapping(path = "register")
    public void registerCustomer() {
        log.info("start register customer");
        abcService.doSomething();

    }
}
