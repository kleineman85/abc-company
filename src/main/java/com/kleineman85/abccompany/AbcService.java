package com.kleineman85.abccompany;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbcService {

    public void doSomething() {
        log.info("start do something");

    }

}
