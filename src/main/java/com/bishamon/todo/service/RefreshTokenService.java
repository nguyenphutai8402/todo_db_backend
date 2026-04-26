package com.bishamon.todo.service;

import com.bishamon.todo.entity.RefreshToken;
import com.bishamon.todo.entity.User;
import io.jsonwebtoken.Claims;

public interface RefreshTokenService {
    RefreshToken store(User user, String rawToken, Claims claims);
    RefreshToken validateAndGet(String rawToken, Claims claims);
    RefreshToken rotate(RefreshToken oldToken, User user, String newRawToken, Claims newClaims);
    void revokeIfPresent(String rawToken, Claims claims);}
