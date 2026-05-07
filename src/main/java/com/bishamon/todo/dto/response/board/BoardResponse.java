package com.bishamon.todo.dto.response.board;

import com.bishamon.todo.dto.response.user.UserSummaryResponse;
import com.bishamon.todo.dto.response.workspace.WorkspaceSummaryResponse;
import com.bishamon.todo.entity.BoardMember;
import com.bishamon.todo.entity.TaskList;
import com.bishamon.todo.enumeration.BoardVisibility;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardResponse {
    Long id;
    WorkspaceSummaryResponse workspace;
    String name;
    String description;
    BoardVisibility visibility;
    boolean isArchived;
    UserSummaryResponse createdBy;
}
