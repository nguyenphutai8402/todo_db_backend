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
import com.bishamon.todo.repository.RefreshTokenRepository;
import com.bishamon.todo.repository.UserRepository;
import com.bishamon.todo.security.user.CustomUserDetails;
import com.bishamon.todo.security.jwt.JwtTokenProvider;
import com.bishamon.todo.service.AuthService;
import com.bishamon.todo.util.CookieService;
import com.bishamon.todo.util.TokenHashService;
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

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthServiceImpl implements AuthService {
    AuthenticationManager authenticationManager;
    JwtTokenProvider jwtTokenProvider;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AuthMapper authMapper;
    CookieService cookieService;
    TokenHashService tokenHashService;
    RefreshTokenRepository refreshTokenRepository;

    @Value("${refresh-token-expiration}")
    @NonFinal
    long refreshTokenExpiration;


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
        return issueToken(user, response);
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
        return issueToken(user, response);
    }

    @Override
    public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieService.getRefreshToken(request).
                orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (jwtTokenProvider.getTokenType(refreshToken) != TokenType.REFRESH) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN_TYPE);
        }

        String jti = jwtTokenProvider.getJtiFromToken(refreshToken);
        RefreshToken storedToken = refreshTokenRepository.findByJti(jti).orElseThrow(
                () -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)
        );

        if (storedToken.isRevoked()) {
            throw new AppException(ErrorCode.REFRESH_TOKEN_REVOKED);
        }

        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            throw new AppException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        if (!tokenHashService.matches(refreshToken, storedToken.getTokenHash())) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        User user = storedToken.getUser();
        CustomUserDetails customUserDetails = CustomUserDetails.from(user);

        String newAccessToken = jwtTokenProvider.generateAccessToken(customUserDetails);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(customUserDetails);

        RefreshToken token = RefreshToken.builder()
                .jti(jwtTokenProvider.getJtiFromToken(newRefreshToken))
                .tokenHash(tokenHashService.hash(newRefreshToken))
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .revoked(false)
                .user(user)
                .build();

        refreshTokenRepository.save(token);

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookieService.createRefreshToken(newRefreshToken, refreshTokenExpiration).toString()
        );

        AuthResponse authResponse = authMapper.toAuthResponse(customUserDetails);
        authResponse.setAccessToken(newAccessToken);

        return authResponse;
    }

    public AuthResponse issueToken(User user, HttpServletResponse response) {
        CustomUserDetails customUserDetails = CustomUserDetails.from(user);

        String accessToken = jwtTokenProvider.generateAccessToken(customUserDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(customUserDetails);

        RefreshToken storedToken = RefreshToken.builder()
                .jti(jwtTokenProvider.getJtiFromToken(refreshToken))
                .tokenHash(tokenHashService.hash(refreshToken))
                .revoked(false)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .user(user)
                .build();
        refreshTokenRepository.save(storedToken);

        ResponseCookie cookie = cookieService.createRefreshToken(refreshToken, refreshTokenExpiration);

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        AuthResponse authResponse = authMapper.toAuthResponse(customUserDetails);
        authResponse.setAccessToken(accessToken);
        return authResponse;
    }
}
