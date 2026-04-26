package com.bishamon.todo.service.impl;

import com.bishamon.todo.entity.BlacklistedAccessToken;
import com.bishamon.todo.repository.BlacklistedAccessTokenRepository;
import com.bishamon.todo.service.BlackListedAccessTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BlackListedAccessTokenServiceImpl implements BlackListedAccessTokenService {
    BlacklistedAccessTokenRepository blacklistedAccessTokenRepository;

    @Override
    public void blacklist(String jti, Instant expiryDate) {
        if (!StringUtils.hasText(jti)) {
            log.warn("Attempted to blacklist token with empty jti");
            return;
        }
        try {
            blacklistedAccessTokenRepository.save(BlacklistedAccessToken.builder()
                    .jti(jti)
                    .expiryDate(expiryDate)
                    .build());
            log.debug("Access token blacklisted: jti={}", jti);
        }catch (DataIntegrityViolationException e){
            log.debug("Token already blacklisted: jti={}", jti);        }
    }

    @Override
    public boolean isBlacklisted(String jti) {
        if(!StringUtils.hasText(jti)) return false;
        return blacklistedAccessTokenRepository.existsByJti(jti);
    }
}
