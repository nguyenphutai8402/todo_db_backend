package com.bishamon.todo.controller;


import com.bishamon.todo.dto.request.board.CreateBoardRequest;
import com.bishamon.todo.dto.response.board.BoardResponse;
import com.bishamon.todo.dto.response.common.ApiResponse;
import com.bishamon.todo.security.user.CustomUserDetails;
import com.bishamon.todo.service.BoardService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/boards")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardController {
    BoardService boardService;

    @PostMapping()
    public ResponseEntity<ApiResponse<BoardResponse>> create(
            @PathVariable Long workspaceId,
            @Valid @RequestBody CreateBoardRequest createBoardRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        BoardResponse boardResponse = boardService.createdBoard(createBoardRequest, workspaceId, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.created(boardResponse));
    }
}
