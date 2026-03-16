package com.bishamon.todo.service.impl;

import com.bishamon.todo.dto.request.CreateWorkspaceRequest;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;
import com.bishamon.todo.entity.User;
import com.bishamon.todo.entity.Workspace;
import com.bishamon.todo.entity.WorkspaceMember;
import com.bishamon.todo.enumeration.ContextualRole;
import com.bishamon.todo.enumeration.ErrorCode;
import com.bishamon.todo.enumeration.Visibility;
import com.bishamon.todo.exception.AppException;
import com.bishamon.todo.mapper.WorkspaceMapper;
import com.bishamon.todo.repository.UserRepository;
import com.bishamon.todo.repository.WorkspaceRepository;
import com.bishamon.todo.service.WorkspaceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkspaceServiceImpl implements WorkspaceService {
    UserRepository userRepository;
    WorkspaceRepository workspaceRepository;
    WorkspaceMapper workspaceMapper;

    @Override
    @Transactional
    public WorkspaceSummaryResponse createWorkSpace(Long userId, CreateWorkspaceRequest createWorkSpaceRequest) {
        User currentUser =  userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (workspaceRepository.existsByCreatedByAndNameIgnoreCase(currentUser, createWorkSpaceRequest.getName())) {
            throw new AppException(ErrorCode.WORKSPACE_NAME_DUPLICATE);
        }

        Workspace workspace = workspaceMapper.toWorkspace(createWorkSpaceRequest);
        if (workspace.getVisibility() == null) workspace.setVisibility(Visibility.PRIVATE);

        workspace.setCreatedBy(currentUser);

        WorkspaceMember ownerMember = WorkspaceMember.builder()
                .user(currentUser)
                .contextualRole(ContextualRole.OWNER)
                .build();
        workspace.addMember(ownerMember);

        Workspace savedWorkspace = workspaceRepository.save(workspace);

        return workspaceMapper.toWorkSpaceSummaryResponse(savedWorkspace);
    }
}
