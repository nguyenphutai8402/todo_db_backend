package com.bishamon.todo.service;

import com.bishamon.todo.dto.request.CreateWorkspaceRequest;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;

public interface WorkspaceService {
    WorkspaceSummaryResponse createWorkSpace(CreateWorkspaceRequest createWorkSpaceRequest);
}
