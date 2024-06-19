package com.kleineman85.abccompany;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AbcExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AbcErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        AbcErrorResponse abcErrorResponse = new AbcErrorResponse("FE01", e.getMessage());

        return new ResponseEntity<>(abcErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AbcErrorResponse> handleException(Exception e) {
        AbcErrorResponse abcErrorResponse = new AbcErrorResponse("TE01", "An unexpected error occured, for questions contact support@abccompany.com");

        return new ResponseEntity<>(abcErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}