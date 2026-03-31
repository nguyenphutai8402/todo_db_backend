package com.bishamon.todo.service.impl;

import com.bishamon.todo.dto.request.auth.LoginRequest;
import com.bishamon.todo.dto.request.auth.RegisterRequest;
import com.bishamon.todo.dto.response.auth.AuthResponse;
import com.bishamon.todo.entity.User;
import com.bishamon.todo.enumeration.code.ErrorCode;
import com.bishamon.todo.exception.AppException;
import com.bishamon.todo.mapper.AuthMapper;
import com.bishamon.todo.repository.UserRepository;
import com.bishamon.todo.security.CustomUserDetails;
import com.bishamon.todo.security.JwtTokenProvider;
import com.bishamon.todo.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    AuthenticationManager authenticationManager;
    JwtTokenProvider jwtTokenProvider;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AuthMapper authMapper;

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = authMapper.toUser(registerRequest);
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getPassword()
                )
        );

//        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        AuthResponse authResponse = authMapper.toAuthResponse(customUserDetails);
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);

        return authResponse;
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        AuthResponse authResponse = authMapper.toAuthResponse(customUserDetails);
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        return authResponse;
    }


}
