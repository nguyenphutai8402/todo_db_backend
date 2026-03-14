package com.bishamon.todo.service.impl;

import com.bishamon.todo.dto.request.CreateWorkspaceRequest;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;
import com.bishamon.todo.entity.User;
import com.bishamon.todo.entity.Workspace;
import com.bishamon.todo.entity.WorkspaceMember;
import com.bishamon.todo.enumeration.ContextualRole;
import com.bishamon.todo.enumeration.Visibility;
import com.bishamon.todo.mapper.WorkspaceMapper;
import com.bishamon.todo.repository.UserRepository;
import com.bishamon.todo.repository.WorkspaceRepository;
import com.bishamon.todo.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMapper workspaceMapper;

    @Override
    @Transactional
    public WorkspaceSummaryResponse createWorkSpace(Long userId, CreateWorkspaceRequest createWorkSpaceRequest) {
        User currentUser =  userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("not found"));
        if (workspaceRepository.existsByNameAndCreatedBy(createWorkSpaceRequest.getName(), currentUser)) {
            throw new RuntimeException("You already have a workspace with this name");
        }

        Workspace workspace = workspaceMapper.toWorkspace(createWorkSpaceRequest);
        if (workspace.getVisibility() == null) workspace.setVisibility(Visibility.PRIVATE);

        workspace.setCreatedBy(currentUser);

        WorkspaceMember ownerMember = WorkspaceMember.builder()
                .user(currentUser)
                .contextualRole(ContextualRole.OWNER)
                .joinedAt(LocalDateTime.now())
                .build();
        workspace.addMember(ownerMember);

        workspaceRepository.save(workspace);

        return workspaceMapper.toWorkSpaceSummaryResponse(workspace);
    }
}
