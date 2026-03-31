package com.bishamon.todo.mapper;

import com.bishamon.todo.dto.request.workspace.CreateWorkspaceRequest;
import com.bishamon.todo.dto.response.workspace.CreateWorkspaceResponse;
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
    CreateWorkspaceResponse toWorkspaceCreationResponse(Workspace workspace);

    WorkspaceSummaryResponse toWorkspaceSummaryResponse(Workspace workspace);
    List<WorkspaceSummaryResponse> toWorkspaceSummaryResponseList(List<Workspace> workspaces);

    WorkspaceDetailResponse toWorkspaceDetailResponse(Workspace workspace);

    @Named("countMember")
    default int countMember(Set<WorkspaceMember> members){
        return members != null ? members.size() : 0;
    }
}
