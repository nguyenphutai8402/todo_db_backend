package com.bishamon.todo.service.impl;

import com.bishamon.todo.entity.Board;
import com.bishamon.todo.repository.BoardRepository;
import com.bishamon.todo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Override
    public Board createBoard() {
        return null;
    }
}
