package com.bishamon.todo.service;

import com.bishamon.todo.dto.request.workspace.WorkspaceRequest;
import com.bishamon.todo.dto.response.workspace.WorkspaceDetailResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkspaceService {
    WorkspaceResponse createWorkspace(WorkspaceRequest workSpaceRequest, Long currentUserId);
    List<WorkspaceSummaryResponse> getMyWorkspaces(Long currentUserId);
    WorkspaceResponse updateWorkspace(
            Long workspaceId,  Long currentUserId, WorkspaceRequest workspaceRequest);
    WorkspaceResponse updateWorkspaceLogo(
            Long workspaceId, Long currentUserId, MultipartFile file);
    void deleteWorkspace(Long workspaceId, Long currentUserId);
}
