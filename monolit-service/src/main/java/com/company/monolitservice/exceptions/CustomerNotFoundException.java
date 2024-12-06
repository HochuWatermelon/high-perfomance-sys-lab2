package com.company.monolitservice.exceptions;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends HttpException {
    public CustomerNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}