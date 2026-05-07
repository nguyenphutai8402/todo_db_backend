package com.bishamon.todo.service.impl;

import com.bishamon.todo.dto.request.workspace.WorkspaceRequest;
import com.bishamon.todo.dto.response.workspace.WorkspaceResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;
import com.bishamon.todo.entity.User;
import com.bishamon.todo.entity.Workspace;
import com.bishamon.todo.entity.WorkspaceMember;
import com.bishamon.todo.enumeration.ContextualRole;
import com.bishamon.todo.enumeration.WorkspaceVisibility;
import com.bishamon.todo.enumeration.code.ErrorCode;
import com.bishamon.todo.exception.AppException;
import com.bishamon.todo.mapper.WorkspaceMapper;
import com.bishamon.todo.repository.UserRepository;
import com.bishamon.todo.repository.WorkspaceMemberRepository;
import com.bishamon.todo.repository.WorkspaceRepository;
import com.bishamon.todo.service.CloudinaryService;
import com.bishamon.todo.service.ImageService;
import com.bishamon.todo.service.WorkspaceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {
    UserRepository userRepository;
    WorkspaceRepository workspaceRepository;
    WorkspaceMemberRepository workspaceMemberRepository;
    WorkspaceMapper workspaceMapper;
    ImageService imageService;
    CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public WorkspaceResponse createWorkspace(WorkspaceRequest workSpaceRequest, Long currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (workspaceRepository.existsByCreatedByAndName(currentUser, workSpaceRequest.getName())) {
            throw new AppException(ErrorCode.WORKSPACE_NAME_DUPLICATE);
        }

        Workspace workspace = workspaceMapper.toWorkspace(workSpaceRequest);
        if (workspace.getWorkspaceVisibility() == null) workspace.setWorkspaceVisibility(WorkspaceVisibility.PRIVATE);

        if (workspace.getLogoUrl() == null) workspace.setLogoUrl(imageService.generateImageUrl(workspace.getName()));
        workspace.setCreatedBy(currentUser);

        WorkspaceMember ownerMember = WorkspaceMember.builder()
                .user(currentUser)
                .contextualRole(ContextualRole.OWNER)
                .build();
        workspace.addMember(ownerMember);

        Workspace savedWorkspace = workspaceRepository.save(workspace);

        return workspaceMapper.toWorkspaceResponse(savedWorkspace);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkspaceSummaryResponse> getMyWorkspaces(Long currentUserId) {
        List<Workspace> listWorkspace = workspaceRepository.findAllByUserId(currentUserId);
        return workspaceMapper.toWorkspaceSummaryResponseList(listWorkspace);
    }

    @Override
    @Transactional
    public WorkspaceResponse updateWorkspace(
            Long workspaceId, Long currentUserId, WorkspaceRequest workspaceRequest) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new AppException(ErrorCode.WORKSPACE_NOT_FOUND));

        WorkspaceMember workspaceMember = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_A_MEMBER));

        if (workspaceMember.getContextualRole() != ContextualRole.OWNER &&
                workspaceMember.getContextualRole() != ContextualRole.MANAGER) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        if (!workspace.getName().equals(workspaceRequest.getName()) &&
                workspaceRepository.existsByCreatedByAndNameAndIdNot(
                        workspace.getCreatedBy(), workspaceRequest.getName(), workspace.getId())) {
            throw new AppException(ErrorCode.WORKSPACE_NAME_DUPLICATE);
        }

        workspaceMapper.updateWorkspace(workspace, workspaceRequest);
        Workspace updateWorkspace = workspaceRepository.save(workspace);
        return workspaceMapper.toWorkspaceResponse(updateWorkspace);
    }

    @Override
    @Transactional
    public WorkspaceResponse updateWorkspaceLogo(Long workspaceId, Long currentUserId, MultipartFile file) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new AppException(ErrorCode.WORKSPACE_NOT_FOUND));

        WorkspaceMember workspaceMember = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_A_MEMBER));

        if (workspaceMember.getContextualRole() != ContextualRole.OWNER &&
                workspaceMember.getContextualRole() != ContextualRole.MANAGER) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        String logoUrl = cloudinaryService.uploadWorkspaceLogo(file, workspaceId);
        workspace.setLogoUrl(logoUrl);
        workspaceRepository.save(workspace);
        return workspaceMapper.toWorkspaceResponse(workspace);
    }

    @Override
    @Transactional
    public void deleteWorkspace(Long workspaceId, Long currentUserId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new AppException(ErrorCode.WORKSPACE_NOT_FOUND));

        WorkspaceMember workspaceMember = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_A_MEMBER));

        if(workspaceMember.getContextualRole() != ContextualRole.OWNER){
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        workspaceRepository.delete(workspace);
    }
}
