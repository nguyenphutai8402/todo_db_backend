package com.bishamon.todo.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CookieService {
    @Value("${app.cookie.refresh-name}")
    String refreshCookieName;

    @Value("${app.cookie.refresh-path}")
    String refreshCookiePath;

    @Value("${app.cookie.same-site}")
    String sameSite;

    @Value("${app.cookie.secure}")
    boolean secure;

    public ResponseCookie createRefreshToken(String token, long maxAgeMs){
        return ResponseCookie.from(refreshCookieName, token)
                .httpOnly(true)
                .secure(secure)
                .path(refreshCookiePath)
                .sameSite(sameSite)
                .maxAge(Duration.ofMillis(maxAgeMs))
                .build();
    }

    public ResponseCookie deleteRefreshCookie() {
        return ResponseCookie.from(refreshCookieName, "")
                .httpOnly(true)
                .secure(secure)
                .path(refreshCookiePath)
                .sameSite(sameSite)
                .maxAge(Duration.ZERO)
                .build();
    }

    public Optional<String> getRefreshToken(HttpServletRequest request){
        if (request.getCookies() == null) return Optional.empty();

        return Arrays.stream(request.getCookies())
                .filter(cookie -> refreshCookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
