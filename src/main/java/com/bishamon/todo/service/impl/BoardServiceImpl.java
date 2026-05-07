package com.bishamon.todo.service.impl;

import com.bishamon.todo.dto.request.board.CreateBoardRequest;
import com.bishamon.todo.dto.response.board.BoardResponse;
import com.bishamon.todo.entity.*;
import com.bishamon.todo.enumeration.ContextualRole;
import com.bishamon.todo.enumeration.code.ErrorCode;
import com.bishamon.todo.exception.AppException;
import com.bishamon.todo.mapper.BoardMapper;
import com.bishamon.todo.repository.BoardRepository;
import com.bishamon.todo.repository.UserRepository;
import com.bishamon.todo.repository.WorkspaceMemberRepository;
import com.bishamon.todo.repository.WorkspaceRepository;
import com.bishamon.todo.service.BoardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardServiceImpl implements BoardService {
    BoardRepository boardRepository;
    UserRepository userRepository;
    WorkspaceRepository workspaceRepository;
    WorkspaceMemberRepository workspaceMemberRepository;
    BoardMapper boardMapper;

    @Override
    @Transactional
    public BoardResponse createdBoard(CreateBoardRequest createBoardRequest, Long workspaceId, Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new AppException(ErrorCode.WORKSPACE_NOT_FOUND));

        WorkspaceMember workspaceMember = workspaceMemberRepository.findByWorkspaceIdAndUserId(workspaceId, currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_A_MEMBER));

        if (workspaceMember.getContextualRole() == ContextualRole.OBSERVER) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        if (boardRepository.existsByWorkspaceIdAndName(workspaceId, createBoardRequest.getName())) {
            throw new AppException(ErrorCode.BOARD_NAME_DUPLICATE);
        }

        Board board = boardMapper.toBoard(createBoardRequest);

        BoardMember ownerMember = BoardMember.builder()
                .user(user)
                .build();

        board.addMember(ownerMember);
        board.setWorkspace(workspace);
        board.setCreatedBy(user);

        Board saved = boardRepository.save(board);
        return boardMapper.toBoardResponse(saved);
    }
}
