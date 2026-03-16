package com.bishamon.todo.enumeration;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // ============ GENERAL ============
    UNCATEGORIZED(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation failed"),

//    // ============ AUTH ============
//    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Authentication required"),
//    UNAUTHORIZED(HttpStatus.FORBIDDEN, "Access denied"),
//    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token has expired"),
//    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Invalid token"),

    // ============ USER ============
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "User already exists"),
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "Email already in use"),
    USER_PASSWORD_WRONG(HttpStatus.BAD_REQUEST, "Incorrect password"),
    // ============ WORKSPACE ============
    WORKSPACE_NAME_DUPLICATE(HttpStatus.CONFLICT, "This workspace already exists with this user."),
    // ============ RESOURCE ============
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),
    RESOURCE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Resource already exists"),
    ;

    HttpStatusCode httpStatus;
    String message;


    public String getCode() {
        return this.name();
    }
}
