package com.bishamon.todo.controller;

import com.bishamon.todo.dto.request.workspace.WorkspaceRequest;
import com.bishamon.todo.dto.response.common.ApiResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceDetailResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;
import com.bishamon.todo.enumeration.code.SuccessCode;
import com.bishamon.todo.security.user.CustomUserDetails;
import com.bishamon.todo.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/workspaces")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkspaceController {
    WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkspaceDetailResponse>> create(
            @RequestBody @Valid WorkspaceRequest workspaceRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkspaceDetailResponse workspaceResponse = workspaceService.createWorkspace(workspaceRequest, userDetails.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessCode.CREATED, workspaceResponse));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<WorkspaceSummaryResponse>>> getMy(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(workspaceService.getMyWorkspaces(userDetails.getId())));
    }

    @PatchMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<WorkspaceDetailResponse>> update(
            @PathVariable Long workspaceId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody WorkspaceRequest workspaceRequest) {
        WorkspaceDetailResponse workspaceDetailResponse = workspaceService.updateWorkspace(
                workspaceId, userDetails.getId(), workspaceRequest);
        return ResponseEntity.ok(ApiResponse.ok(workspaceDetailResponse));
    }

    @PutMapping(
            value = "/{workspaceId}/logo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<WorkspaceDetailResponse>> updateLogo(
            @PathVariable Long workspaceId,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetail
    ) {
        WorkspaceDetailResponse workspaceDetailResponse =
                workspaceService.updateWorkspaceLogo(workspaceId, userDetail.getId(), file);
        return ResponseEntity.ok(ApiResponse.ok(workspaceDetailResponse));
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long workspaceId,
            @AuthenticationPrincipal CustomUserDetails userDetail
    ){
        workspaceService.deleteWorkspace(workspaceId, userDetail.getId());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK));
    }
}
