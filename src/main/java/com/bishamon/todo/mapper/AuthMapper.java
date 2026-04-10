package com.bishamon.todo.mapper;

import com.bishamon.todo.dto.request.auth.RegisterRequest;
import com.bishamon.todo.dto.response.auth.AuthResponse;
import com.bishamon.todo.entity.User;
import com.bishamon.todo.security.CustomUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "passwordHash", ignore = true)
    User toUser(RegisterRequest registerRequest);

    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    AuthResponse toAuthResponse(CustomUserDetails customUserDetails);
}
