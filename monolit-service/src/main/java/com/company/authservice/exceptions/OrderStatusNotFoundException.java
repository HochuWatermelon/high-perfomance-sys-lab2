package com.company.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class OrderStatusNotFoundException extends HttpException{
    public OrderStatusNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
