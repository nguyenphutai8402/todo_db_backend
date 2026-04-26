package com.bishamon.todo.security.jwt;

import com.bishamon.todo.enumeration.TokenType;
import com.bishamon.todo.security.user.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class JwtTokenProvider {
    static final String CLAIM_TYPE = "type";
    static final String CLAIM_ROLE = "role";

    @Value("${jwt.secret-key}")
    String secretKeyString;
    @Value("${access-token-expiration}")
    long accessTokenExpiration;
    @Value("${refresh-token-expiration}")
    long refreshTokenExpiration;

    SecretKey signingKey;
    JwtParser jwtParser;

    @PostConstruct
    void init() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKeyString);
        signingKey = Keys.hmacShaKeyFor(keyBytes);

        jwtParser = Jwts.parser()
                .verifyWith(signingKey)
                .build();
    }

    public Claims parseClaims(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    public String buildToken(CustomUserDetails customUserDetails, long expiration, TokenType tokenType) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(customUserDetails.getUsername())
                .claim(CLAIM_TYPE, tokenType)
                .claim(CLAIM_ROLE, customUserDetails.getGlobalRole())
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(signingKey)
                .compact();
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        return buildToken(userDetails, accessTokenExpiration, TokenType.ACCESS);
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        return buildToken(userDetails, refreshTokenExpiration, TokenType.REFRESH);
    }

    public String generateAccessToken(Authentication authentication) {
        return generateAccessToken((CustomUserDetails) authentication.getPrincipal());
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateRefreshToken((CustomUserDetails) authentication.getPrincipal());
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String getJti(Claims claims) {
        return claims.getId();
    }

    public String getSubject(Claims claims) {
        return claims.getSubject();
    }

    public TokenType getTokenType(Claims claims) {
        return TokenType.valueOf(claims.get(CLAIM_TYPE, String.class));
    }

    public Instant getExpiration(Claims claims) {
        return claims.getExpiration().toInstant();
    }
}
