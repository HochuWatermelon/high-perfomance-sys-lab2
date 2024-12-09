package com.company.workservice.exceptions;

import org.springframework.http.HttpStatus;

public class ProfessionNotFoundException extends HttpException {
    public ProfessionNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}