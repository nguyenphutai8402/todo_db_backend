package com.bishamon.todo.mapper;

import com.bishamon.todo.dto.request.workspace.WorkspaceRequest;
import com.bishamon.todo.dto.response.workspace.WorkspaceDetailResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;
import com.bishamon.todo.entity.Workspace;
import com.bishamon.todo.entity.WorkspaceMember;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, WorkspaceMemberMapper.class}
)
public interface WorkspaceMapper {
    // ============ REQUEST ============
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Workspace toWorkspace(WorkspaceRequest workspaceRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateWorkspace(@MappingTarget Workspace workspace, WorkspaceRequest request);

    // ============ RESPONSE============
    @Mapping(target = "memberCount", source = "members", qualifiedByName = "countMember")
    WorkspaceDetailResponse toWorkspaceDetailResponse(Workspace workspace);

    WorkspaceSummaryResponse toWorkspaceSummaryResponse(Workspace workspace);
    List<WorkspaceSummaryResponse> toWorkspaceSummaryResponseList(List<Workspace> workspaces);

    @Named("countMember")
    default int countMember(Set<WorkspaceMember> members){
        return members != null ? members.size() : 0;
    }
}
