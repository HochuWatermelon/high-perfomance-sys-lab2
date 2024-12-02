package com.company.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends HttpException {
    public CustomerNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}