package com.company.monolitservice.exceptions;

import org.springframework.http.HttpStatus;

public class UniqueFieldAlreadyExistException extends HttpException{
    public UniqueFieldAlreadyExistException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
