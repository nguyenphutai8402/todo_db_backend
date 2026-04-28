package com.bishamon.todo.mapper;

import com.bishamon.todo.dto.response.user.UserSummaryResponse;
import com.bishamon.todo.entity.WorkspaceMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface WorkspaceMemberMapper {
    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.avatarUrl", target = "avatarUrl")
    UserSummaryResponse toUserSummaryResponse(WorkspaceMember workspaceMember);
    List<UserSummaryResponse> toUserSummaryResponseList(Set<WorkspaceMember> members);
}
