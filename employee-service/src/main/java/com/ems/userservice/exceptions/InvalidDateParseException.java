package com.ems.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateParseException extends RuntimeException {

    public InvalidDateParseException(String date) {
        super("Invalid date format - " + date);
    }
}
