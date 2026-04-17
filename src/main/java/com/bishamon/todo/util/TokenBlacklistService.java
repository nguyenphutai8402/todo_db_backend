package com.bishamon.todo.util;

import com.bishamon.todo.entity.BlacklistedAccessToken;
import com.bishamon.todo.repository.BlacklistedAccessTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenBlacklistService {
    BlacklistedAccessTokenRepository blacklistedAccessTokenRepository;

    public void blacklist(String jti, Instant expiryDate) {
        if (!StringUtils.hasText(jti)) {
            return;
        }
        if (blacklistedAccessTokenRepository.existsByJti(jti)) {
            return;
        }
        blacklistedAccessTokenRepository.save(BlacklistedAccessToken.builder()
                        .jti(jti)
                        .expiryDate(expiryDate)
                        .build());
    }
    public boolean isBlacklisted(String jti) {
        return StringUtils.hasText(jti) && blacklistedAccessTokenRepository.existsByJti(jti);
    }
}
