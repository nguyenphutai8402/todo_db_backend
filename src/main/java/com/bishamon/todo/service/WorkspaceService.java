package com.bishamon.todo.service;

import com.bishamon.todo.dto.request.CreateWorkspaceRequest;
import com.bishamon.todo.dto.response.workspace.WorkspaceDetailResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;

import java.util.List;

public interface WorkspaceService {
    WorkspaceSummaryResponse createWorkspace(CreateWorkspaceRequest createWorkSpaceRequest);
    List<WorkspaceDetailResponse> getMyWorkspaces();
}
