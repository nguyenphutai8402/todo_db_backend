package com.bishamon.todo.service;

import com.bishamon.todo.dto.request.auth.LoginRequest;
import com.bishamon.todo.dto.request.auth.RegisterRequest;
import com.bishamon.todo.dto.response.auth.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest, HttpServletResponse response);
    AuthResponse login(LoginRequest loginRequest, HttpServletResponse response);
    AuthResponse refresh(HttpServletRequest request, HttpServletResponse httpServletResponse);
}
