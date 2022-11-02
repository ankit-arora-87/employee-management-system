package com.ems.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UniqueLoginException extends RuntimeException {

    public UniqueLoginException(String login) {
        super("Employee login not unique - " + login);
    }
}
