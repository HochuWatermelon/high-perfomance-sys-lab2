package com.company.orderservice.exceptions;

import org.springframework.http.HttpStatus;

public class WaitingWorkerNotFoundException extends HttpException {
    public WaitingWorkerNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
