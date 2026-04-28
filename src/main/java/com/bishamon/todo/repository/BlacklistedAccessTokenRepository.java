package com.bishamon.todo.repository;

import com.bishamon.todo.entity.BlacklistedAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface BlacklistedAccessTokenRepository extends JpaRepository<BlacklistedAccessToken, Long> {
    boolean existsByJti(String jti);

    @Modifying
    @Query("""
            DELETE FROM BlacklistedAccessToken b WHERE b.expiryDate < :now
            """)
    int deleteByExpiryDateBefore(@Param("now") Instant now);
}
