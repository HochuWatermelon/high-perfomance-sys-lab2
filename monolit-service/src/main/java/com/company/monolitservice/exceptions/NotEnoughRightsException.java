package com.company.monolitservice.exceptions;

import org.springframework.http.HttpStatus;

public class NotEnoughRightsException extends HttpException{
    public NotEnoughRightsException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
