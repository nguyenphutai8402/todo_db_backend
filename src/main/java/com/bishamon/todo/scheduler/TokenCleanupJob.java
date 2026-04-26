package com.bishamon.todo.scheduler;

import com.bishamon.todo.repository.BlacklistedAccessTokenRepository;
import com.bishamon.todo.repository.RefreshTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TokenCleanupJob {
    RefreshTokenRepository refreshTokenRepository;
    BlacklistedAccessTokenRepository blacklistedAccessTokenRepository;

    @Scheduled(cron = "${app.jobs.token-cleanup-cron}",
            zone = "${app.jobs.time-zone}")
    @Transactional
    public void cleanupExpiryTokens() {
        Instant now = Instant.now();
        int deletedRefresh = refreshTokenRepository.deleteByExpiryDateBefore(now);
        int deletedBlacklisted = blacklistedAccessTokenRepository.deleteByExpiryDateBefore(now);
        log.info("Token cleanup done: removed {} refresh tokens, {} blacklisted access tokens",
                deletedRefresh, deletedBlacklisted);
    }
}
