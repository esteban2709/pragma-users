package com.pragma.usersservice.domain.exception;

public enum ExceptionMessage {
    ONLY_ADMIN_CAN_CREATE_OWNER("Only administrators can create owner accounts"),
    ONLY_OWNER_CAN_CREATE_EMPLOYEE("Only owners can create employee accounts"),;

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
