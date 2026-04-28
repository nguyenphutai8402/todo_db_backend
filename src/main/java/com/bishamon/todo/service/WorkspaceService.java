package com.bishamon.todo.service;

import com.bishamon.todo.dto.request.workspace.WorkspaceRequest;
import com.bishamon.todo.dto.response.workspace.WorkspaceDetailResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;

import java.util.List;

public interface WorkspaceService {
    WorkspaceDetailResponse createWorkspace(WorkspaceRequest workSpaceRequest, Long currentUserId);
    List<WorkspaceSummaryResponse> getMyWorkspaces(Long currentUserId);
    WorkspaceDetailResponse updateWorkspace(
            Long workspaceId,  Long currentUserId, WorkspaceRequest workspaceRequest);
}
