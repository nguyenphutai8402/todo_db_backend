package com.bishamon.todo.service.impl;

import com.bishamon.todo.dto.request.workspace.CreateWorkspaceRequest;
import com.bishamon.todo.dto.response.workspace.CreateWorkspaceResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;
import com.bishamon.todo.entity.User;
import com.bishamon.todo.entity.Workspace;
import com.bishamon.todo.entity.WorkspaceMember;
import com.bishamon.todo.enumeration.ContextualRole;
import com.bishamon.todo.enumeration.code.ErrorCode;
import com.bishamon.todo.enumeration.Visibility;
import com.bishamon.todo.exception.AppException;
import com.bishamon.todo.mapper.WorkspaceMapper;
import com.bishamon.todo.repository.UserRepository;
import com.bishamon.todo.repository.WorkspaceRepository;
import com.bishamon.todo.security.CurrentUserProvider;
import com.bishamon.todo.security.CustomUserDetails;
import com.bishamon.todo.service.WorkspaceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkspaceServiceImpl implements WorkspaceService {
    UserRepository userRepository;
    WorkspaceRepository workspaceRepository;
    WorkspaceMapper workspaceMapper;

    @Override
    @Transactional
    public CreateWorkspaceResponse createWorkspace(CreateWorkspaceRequest createWorkSpaceRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        User currentUser = userRepository.findById(customUserDetails.getId())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
//        User currentUser = currentUserProvider.getCurrentUser();

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

        return workspaceMapper.toWorkspaceCreationResponse(savedWorkspace);
    }

    @Override
    public List<WorkspaceSummaryResponse> getMyWorkspaces() {
        CustomUserDetails customUserDetails = (CustomUserDetails) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        List<Workspace> listWorkspace = workspaceRepository.findAllByUserId(customUserDetails.getId());
        return workspaceMapper.toWorkspaceSummaryResponseList(listWorkspace);
    }
}
