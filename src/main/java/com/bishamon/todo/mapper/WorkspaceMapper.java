package com.bishamon.todo.mapper;

import com.bishamon.todo.dto.request.CreateWorkspaceRequest;
import com.bishamon.todo.dto.response.workspace.WorkspaceMemberResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceDetailResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;
import com.bishamon.todo.entity.Workspace;
import com.bishamon.todo.entity.WorkspaceMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, WorkspaceMemberMapper.class}
)
public interface WorkspaceMapper {
    // ============ REQUEST ============
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Workspace toWorkspace(CreateWorkspaceRequest createWorkSpaceRequest);

    // ============ RESPONSE============
    @Mapping(target = "memberCount", source = "members", qualifiedByName = "countMember")
    WorkspaceSummaryResponse toWorkspaceSummaryResponse(Workspace workspace);

    @Mapping(target = "createdBy", source = "createdBy.id")
    @Mapping(target = "memberCount", source = "members", qualifiedByName = "countMember")
    @Mapping(source = "members", target = "members")
    WorkspaceDetailResponse toWorkspaceDetailResponse(Workspace workspace);
    List<WorkspaceDetailResponse> toWorkspaceDetailResponseList(List<Workspace> workspaces);

    @Named("countMember")
    default int countMember(Set<WorkspaceMember> members){
        return members != null ? members.size() : 0;
    }
}
