package com.bishamon.todo.service.impl;

import com.bishamon.todo.entity.RefreshToken;
import com.bishamon.todo.entity.User;
import com.bishamon.todo.enumeration.code.ErrorCode;
import com.bishamon.todo.exception.AppException;
import com.bishamon.todo.repository.RefreshTokenRepository;
import com.bishamon.todo.security.TokenHashService;
import com.bishamon.todo.security.jwt.JwtTokenProvider;
import com.bishamon.todo.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    RefreshTokenRepository refreshTokenRepository;
    TokenHashService tokenHashService;
    JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public RefreshToken store(User user, String rawToken, Claims claims) {
        RefreshToken entity = RefreshToken.builder()
                .jti(jwtTokenProvider.getJti(claims))
                .tokenHash(tokenHashService.hash(rawToken))
                .revoked(false)
                .expiryDate(jwtTokenProvider.getExpiration(claims))
                .user(user)
                .build();
        return refreshTokenRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken validateAndGet(String rawToken, Claims claims) {
        String jti = jwtTokenProvider.getJti(claims);
        RefreshToken stored = refreshTokenRepository.findByJti(jti)
                .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        if (stored.isRevoked()) {
            log.warn("Attempted to use revoked refresh token: jti={}", jti);
            throw new AppException(ErrorCode.REFRESH_TOKEN_REVOKED);
        }
        if (stored.getExpiryDate().isBefore(Instant.now())) {
            log.warn("I attempted to use expired refresh token.: jti={}", jti);
            throw new AppException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }
        if (!tokenHashService.matches(rawToken, stored.getTokenHash())) {
            log.warn("Refresh token hash mismatch: jti={}", jti);
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        return stored;
    }

    @Override
    @Transactional
    public RefreshToken rotate(RefreshToken oldToken, User user, String newRawToken, Claims newClaims) {
        oldToken.revoke();
        refreshTokenRepository.save(oldToken);
        return store(user, newRawToken, newClaims);
    }

    @Override
    @Transactional
    public void revokeIfPresent(String rawToken, Claims claims) {
        String jti = jwtTokenProvider.getJti(claims);
        refreshTokenRepository.findByJti(jti).ifPresent(stored -> {
            if (tokenHashService.matches(rawToken, stored.getTokenHash())) {
                stored.revoke();
                refreshTokenRepository.save(stored);
                log.debug("Refresh token revoked on logout: jti={}", jti);
            } else {
                log.warn("Hash mismatch during logout revoke: jti={}", jti);
            }
        });
    }
}
