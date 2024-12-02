package com.company.authservice.exceptions;

import org.springframework.http.HttpStatus;

public class FeedbackNotFoundException extends HttpException {
    public FeedbackNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}