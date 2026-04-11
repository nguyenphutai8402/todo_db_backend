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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
        if(!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())){
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }
        User user = authMapper.toUser(registerRequest);
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        try {
            user = userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        CustomUserDetails customUserDetails = CustomUserDetails.from(user);
        String accessToken = jwtTokenProvider.generateAccessToken(customUserDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(customUserDetails);
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
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        AuthResponse authResponse = authMapper.toAuthResponse(customUserDetails);
        authResponse.setAccessToken(jwtTokenProvider.generateAccessToken(authentication));
        authResponse.setRefreshToken(jwtTokenProvider.generateRefreshToken(authentication));
        return authResponse;
    }
}
