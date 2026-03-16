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
public enum SuccessCode {
    OK(HttpStatus.OK, "Success"),
    CREATED(HttpStatus.CREATED, "Created successfully"),
    UPDATED(HttpStatus.OK, "Updated successfully"),
    DELETED(HttpStatus.OK, "Deleted successfully"),
    LOGIN_SUCCESS(HttpStatus.OK, "Login successful"),
    LOGOUT_SUCCESS(HttpStatus.OK, "Logout successful"),
    ;

    HttpStatusCode httpStatus;
    String message;

    public String getCode() {
        return this.name();
    }
}
