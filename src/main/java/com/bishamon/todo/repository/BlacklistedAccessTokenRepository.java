package com.bishamon.todo.repository;

import com.bishamon.todo.entity.BlacklistedAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface BlacklistedAccessTokenRepository extends JpaRepository<BlacklistedAccessToken, Long> {
    boolean existsByJti(String jti);
    void deleteByExpiryDateBefore(Instant now);
}
