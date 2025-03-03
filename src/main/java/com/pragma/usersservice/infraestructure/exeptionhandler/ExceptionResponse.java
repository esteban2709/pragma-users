package com.pragma.usersservice.infraestructure.exeptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data was found for the requested operation"),
    USER_NOT_LEGAL_AGE("El usuario debe ser mayor de 18 años para registrarse");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}