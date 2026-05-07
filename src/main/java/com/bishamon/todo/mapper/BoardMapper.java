package com.bishamon.todo.mapper;

import com.bishamon.todo.dto.request.board.CreateBoardRequest;
import com.bishamon.todo.dto.response.board.BoardResponse;
import com.bishamon.todo.entity.Board;
import com.bishamon.todo.entity.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {WorkspaceMapper.class, UserMapper.class})
public interface BoardMapper {
    // REQUEST
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "lists", ignore = true)
    Board toBoard(CreateBoardRequest createBoardRequest);

    // RESPONSE
    BoardResponse toBoardResponse(Board board);
}
