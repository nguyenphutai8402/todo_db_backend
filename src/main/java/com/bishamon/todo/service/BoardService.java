package com.bishamon.todo.service;

import com.bishamon.todo.dto.request.board.CreateBoardRequest;
import com.bishamon.todo.dto.response.board.BoardResponse;
import com.bishamon.todo.entity.Board;

public interface BoardService {
    public BoardResponse createdBoard(CreateBoardRequest createBoardRequest, Long workspaceId, Long currentUserId);
}
