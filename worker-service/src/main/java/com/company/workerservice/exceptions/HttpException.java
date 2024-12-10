package com.company.workerservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String message;

    public HttpException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() { return this.httpStatus;}
    public String getMessage() { return this.message;}
}
