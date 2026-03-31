package com.bishamon.todo.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class JwtTokenProvider {
    @Value("${jwt.secret-key}")
    String secretKeyString;
    @Value("${access-token-expiration}")
    long accessTokenExpiration;
    @Value("${refresh-token-expiration}")
    long refreshTokenExpiration;

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyString);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String buildToken(CustomUserDetails customUserDetails, long expiration, String tokenType){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .subject(customUserDetails.getUsername())
                .claim("type", tokenType)
                .claim("role", customUserDetails.getGlobalRole())
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateAccessToken(Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return buildToken(customUserDetails, accessTokenExpiration, "ACCESS");
    }

    public String generateRefreshToken(Authentication authentication){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return buildToken(customUserDetails, accessTokenExpiration, "REFRESH");
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
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

    public String getEmailFromToken(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getTokenType(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("type", String.class);
    }
}
