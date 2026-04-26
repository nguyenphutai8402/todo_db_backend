package com.bishamon.todo.service.impl;

import com.bishamon.todo.dto.request.auth.LoginRequest;
import com.bishamon.todo.dto.request.auth.RegisterRequest;
import com.bishamon.todo.dto.response.auth.AuthResponse;
import com.bishamon.todo.entity.RefreshToken;
import com.bishamon.todo.entity.User;
import com.bishamon.todo.enumeration.TokenType;
import com.bishamon.todo.enumeration.code.ErrorCode;
import com.bishamon.todo.exception.AppException;
import com.bishamon.todo.mapper.AuthMapper;
import com.bishamon.todo.repository.BlacklistedAccessTokenRepository;
import com.bishamon.todo.repository.RefreshTokenRepository;
import com.bishamon.todo.repository.UserRepository;
import com.bishamon.todo.security.user.CustomUserDetails;
import com.bishamon.todo.security.jwt.JwtTokenProvider;
import com.bishamon.todo.service.AuthService;
import com.bishamon.todo.security.CookieService;
import com.bishamon.todo.security.TokenHashService;
import com.bishamon.todo.service.BlackListedAccessTokenService;
import com.bishamon.todo.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthServiceImpl implements AuthService {
    AuthenticationManager authenticationManager;
    JwtTokenProvider jwtTokenProvider;
    UserRepository userRepository;
    RefreshTokenService refreshTokenService;
    BlackListedAccessTokenService blackListedAccessTokenService;
    CookieService cookieService;
    PasswordEncoder passwordEncoder;
    AuthMapper authMapper;

    @Override
    public AuthResponse register(RegisterRequest registerRequest, HttpServletResponse response) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }
        User user = authMapper.toUser(registerRequest);
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        return issueTokenAndBuildResponse(user, response);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findById(customUserDetails.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return issueTokenAndBuildResponse(user, response);
    }

    @Override
    public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieService.getRefreshToken(request).
                orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Claims refreshClaims = jwtTokenProvider.parseClaims(refreshToken);
        if (jwtTokenProvider.getTokenType(refreshClaims) != TokenType.REFRESH) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN_TYPE);
        }

        RefreshToken storedToken = refreshTokenService.validateAndGet(refreshToken, refreshClaims);

        User user = storedToken.getUser();
        CustomUserDetails customUserDetails = CustomUserDetails.from(user);

        String newAccessToken = jwtTokenProvider.generateAccessToken(customUserDetails);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(customUserDetails);
        Claims newRefeshClaims = jwtTokenProvider.parseClaims(newRefreshToken);
        refreshTokenService.rotate(storedToken, user, newRefreshToken, newRefeshClaims);

        setRefreshTokenCookie(response, newRefreshToken);

        AuthResponse authResponse = authMapper.toAuthResponse(customUserDetails);
        authResponse.setAccessToken(newAccessToken);
        return authResponse;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        cookieService.getRefreshToken(request).ifPresent(refreshToken -> {
            try {
                if(!jwtTokenProvider.validateToken(refreshToken))return;
                Claims refreshClaims = jwtTokenProvider.parseClaims(refreshToken);
                if(jwtTokenProvider.getTokenType(refreshClaims) != TokenType.REFRESH) return;
                refreshTokenService.revokeIfPresent(refreshToken, refreshClaims);
            }catch (Exception e){
                log.warn("Failed to revoke refresh token during logout: {}", e.getMessage());
            }
        });
        String accessToken = resolveBearerToken(request);
        if (accessToken == null) return;
        if (!jwtTokenProvider.validateToken(accessToken)) return;
        Claims accessClaims = jwtTokenProvider.parseClaims(accessToken);
        if (jwtTokenProvider.getTokenType(accessClaims) != TokenType.ACCESS) return;
        blackListedAccessTokenService.blacklist(
                jwtTokenProvider.getJti(accessClaims),
                jwtTokenProvider.getExpiration(accessClaims));
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookieService.deleteRefreshCookie().toString()
        );
    }

    public AuthResponse issueTokenAndBuildResponse(User user, HttpServletResponse response) {
        CustomUserDetails customUserDetails = CustomUserDetails.from(user);

        String accessToken = jwtTokenProvider.generateAccessToken(customUserDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(customUserDetails);

        Claims refreshClaims = jwtTokenProvider.parseClaims(refreshToken);
        refreshTokenService.store(user, refreshToken, refreshClaims);

        setRefreshTokenCookie(response, refreshToken);

        AuthResponse authResponse = authMapper.toAuthResponse(customUserDetails);
        authResponse.setAccessToken(accessToken);
        return authResponse;
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        long expiration = jwtTokenProvider.getRefreshTokenExpiration();
        ResponseCookie cookie = cookieService.createRefreshToken(refreshToken, expiration);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String resolveBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
