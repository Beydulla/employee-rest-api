package com.getwhelp.employeerest.dto.response;

import java.util.HashSet;
import java.util.Set;

public class FieldErrorResponse extends ErrorResponse{

    private final Set<String> fieldErrors;

    public FieldErrorResponse(final int status, final String message){
        super(status, message);
        fieldErrors = new HashSet<>();
    }

    public void addFieldError(final String errorMessage) {
        fieldErrors.add(errorMessage);
    }

    public Set<String> getFieldErrors() {
        return fieldErrors;
    }

}