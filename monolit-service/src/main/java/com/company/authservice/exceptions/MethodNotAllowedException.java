package com.company.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class MethodNotAllowedException extends HttpException{
    public MethodNotAllowedException(String message) {
        super(HttpStatus.METHOD_NOT_ALLOWED, message);
    }
}
