package com.ems.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEmployeeIdException extends RuntimeException {

    public InvalidEmployeeIdException(String id) {
        super("Employee id for user to update & request payload are not matching - " + id);
    }
}
