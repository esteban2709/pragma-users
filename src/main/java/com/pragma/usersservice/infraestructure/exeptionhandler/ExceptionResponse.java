package com.pragma.usersservice.infraestructure.exeptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data was found for the requested operation"),
    USER_NOT_LEGAL_AGE("The user must be over 18 years old to register."),
    USER_NOT_FOUND("The user was not found."),;

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}