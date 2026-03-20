package com.bishamon.todo.controller;

import com.bishamon.todo.dto.request.CreateWorkspaceRequest;
import com.bishamon.todo.dto.response.common.ApiResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceDetailResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;
import com.bishamon.todo.enumeration.code.SuccessCode;
import com.bishamon.todo.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/workspaces")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkspaceController {
    WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkspaceSummaryResponse>> create(
            @RequestBody @Valid CreateWorkspaceRequest createWorkspaceRequest) {
        WorkspaceSummaryResponse workspaceResponse = workspaceService.createWorkspace(createWorkspaceRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.CREATED, workspaceResponse));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<WorkspaceDetailResponse>>> getMy(){
        return ResponseEntity.ok(ApiResponse.ok(workspaceService.getMyWorkspaces()));
    }
}
