package com.company.monolitservice.exceptions;

import org.springframework.http.HttpStatus;

public class WorkerNotFoundException extends HttpException {
    public WorkerNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
