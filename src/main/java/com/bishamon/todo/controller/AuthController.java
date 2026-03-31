package com.bishamon.todo.controller;

import com.bishamon.todo.dto.request.auth.LoginRequest;
import com.bishamon.todo.dto.request.auth.RegisterRequest;
import com.bishamon.todo.dto.response.auth.AuthResponse;
import com.bishamon.todo.dto.response.common.ApiResponse;
import com.bishamon.todo.enumeration.code.SuccessCode;
import com.bishamon.todo.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody @Valid LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(
                ApiResponse.success(SuccessCode.LOGIN_SUCCESS, authResponse)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @RequestBody @Valid RegisterRequest registerRequest) {
        AuthResponse authResponse = authService.register(registerRequest);
        return ResponseEntity.ok(
                ApiResponse.success(SuccessCode.CREATED, authResponse)
        );
    }
}
