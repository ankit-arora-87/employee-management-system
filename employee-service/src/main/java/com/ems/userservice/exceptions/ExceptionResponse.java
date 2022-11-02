package com.ems.userservice.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

public class ExceptionResponse {

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, String>> details;

    public ExceptionResponse(String message) {
        this.message = message;
    }
    public ExceptionResponse(String message, List<Map<String, String>> details) {
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, String>> getDetails() {
        return details;
    }

    public void setDetails(List<Map<String, String>> details) {
        this.details = details;
    }
}
