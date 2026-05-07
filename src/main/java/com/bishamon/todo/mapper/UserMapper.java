package com.bishamon.todo.mapper;

import com.bishamon.todo.dto.response.user.UserSummaryResponse;
import com.bishamon.todo.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // RESPONSE
    UserSummaryResponse toUserSummaryResponse(User user);
}
