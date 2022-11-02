package com.ems.userservice.response;

public class ServiceResponseMessage {

    private String message;

    public ServiceResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
