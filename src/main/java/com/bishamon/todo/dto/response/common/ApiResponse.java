package com.bishamon.todo.dto.response.common;

import com.bishamon.todo.enumeration.code.ErrorCode;
import com.bishamon.todo.enumeration.code.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    boolean success;
    String code;
    String message;
    T data;
    List<FieldError> errors;
    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();

    // ==================== SUCCESS ====================

    public static <T> ApiResponse<T> ok(T data) {
        return buildSuccess(SuccessCode.OK, data);
    }

    public static ApiResponse<Void> ok(String message) {
        return ApiResponse.<Void>builder()
                .success(true)
                .code(SuccessCode.OK.getCode())
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return buildSuccess(SuccessCode.CREATED, data);
    }

    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return buildSuccess(successCode, data);
    }

    public static ApiResponse<Void> success(SuccessCode successCode) {
        return ApiResponse.<Void>builder()
                .success(true)
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build();
    }

    private static <T> ApiResponse<T> buildSuccess(SuccessCode successCode, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .data(data)
                .build();
    }

// ==================== ERROR ====================

    public static ApiResponse<Void> error(ErrorCode errorCode) {
        return ApiResponse.<Void>builder()
                .success(false)
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static ApiResponse<Void> error(ErrorCode errorCode, String customMessage) {
        return ApiResponse.<Void>builder()
                .success(false)
                .code(errorCode.getCode())
                .message(customMessage)
                .build();
    }

    public static ApiResponse<Void> validationError(List<FieldError> errors) {
        return ApiResponse.<Void>builder()
                .success(false)
                .code(ErrorCode.VALIDATION_ERROR.getCode())
                .message(ErrorCode.VALIDATION_ERROR.getMessage())
                .errors(errors)
                .build();
    }
}
