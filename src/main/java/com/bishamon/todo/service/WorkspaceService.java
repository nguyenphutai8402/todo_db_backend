package com.bishamon.todo.service;

import com.bishamon.todo.dto.request.workspace.CreateWorkspaceRequest;
import com.bishamon.todo.dto.response.workspace.CreateWorkspaceResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;

import java.util.List;

public interface WorkspaceService {
    CreateWorkspaceResponse createWorkspace(CreateWorkspaceRequest createWorkSpaceRequest);
    List<WorkspaceSummaryResponse> getMyWorkspaces();
}
