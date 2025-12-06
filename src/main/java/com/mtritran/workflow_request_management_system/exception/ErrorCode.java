package com.mtritran.workflow_request_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    UNDEFINED_EXCEPTION(9999, "Undefine exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(9998, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    USER_NAME_INVALID(1002, "User name must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password must be at least 7 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "You do not have permission", HttpStatus.FORBIDDEN),
    DEFAULT_ROLE_NOT_FOUND(1007, "Default role not found", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1008, "Permission not found", HttpStatus.NOT_FOUND),
    PERMISSION_EXISTED(1009, "Permission already exists", HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(1010, "Role already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1011, "Role not exists", HttpStatus.BAD_REQUEST),
    DOB_INVALID(1012, "User must be at least 18 years old", HttpStatus.BAD_REQUEST),
    REQUEST_NOT_FOUND(2001, "Request not found", HttpStatus.NOT_FOUND),
    REQUEST_ALREADY_PROCESSED(2002, "Request has already been processed", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_TO_APPROVE(2003, "You do not have permission to approve/reject this request", HttpStatus.FORBIDDEN),
    INVALID_REQUEST_TYPE(2004, "Invalid request type", HttpStatus.BAD_REQUEST)
    ;


    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}

