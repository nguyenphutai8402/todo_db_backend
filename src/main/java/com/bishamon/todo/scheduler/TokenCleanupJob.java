package com.bishamon.todo.scheduler;

import com.bishamon.todo.repository.BlacklistedAccessTokenRepository;
import com.bishamon.todo.repository.RefreshTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenCleanupJob {
    RefreshTokenRepository refreshTokenRepository;
    BlacklistedAccessTokenRepository blacklistedAccessTokenRepository;

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    @Transactional
    public void cleanupExpiryTokens(){
        Instant now = Instant.now();
        refreshTokenRepository.deleteByExpiryDateBefore(now);
        blacklistedAccessTokenRepository.deleteByExpiryDateBefore(now);
    }
}
