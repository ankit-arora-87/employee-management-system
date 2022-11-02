package com.ems.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidNumberFormatException extends RuntimeException {

    public InvalidNumberFormatException(String salary) {
        super("Invalid salary - " + salary);
    }
}
