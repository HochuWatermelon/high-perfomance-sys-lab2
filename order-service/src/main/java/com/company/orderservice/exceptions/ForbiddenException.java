package com.company.orderservice.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpException{
    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
