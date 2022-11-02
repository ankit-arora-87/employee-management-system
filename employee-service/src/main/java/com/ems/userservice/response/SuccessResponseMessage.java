package com.ems.userservice.response;

import java.util.HashMap;

public class SuccessResponseMessage {

    private String message;

    public SuccessResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
