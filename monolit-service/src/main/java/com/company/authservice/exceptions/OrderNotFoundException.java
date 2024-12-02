package com.company.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends HttpException{
    public OrderNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
