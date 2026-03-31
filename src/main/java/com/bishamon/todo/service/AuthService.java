package com.bishamon.todo.service;

import com.bishamon.todo.dto.request.auth.LoginRequest;
import com.bishamon.todo.dto.request.auth.RegisterRequest;
import com.bishamon.todo.dto.response.auth.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
