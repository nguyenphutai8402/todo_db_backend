package com.bishamon.todo.service;

import java.time.Instant;

public interface BlackListedAccessTokenService {
    void blacklist(String jti, Instant expiryDate);
    boolean isBlacklisted(String jti);
}
