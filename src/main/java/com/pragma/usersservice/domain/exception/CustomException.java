package com.pragma.usersservice.domain.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
