package com.company.monolitservice.exceptions;

import org.springframework.http.HttpStatus;

public class FeedbackNotFoundException extends HttpException {
    public FeedbackNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}