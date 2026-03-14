package com.bishamon.todo.dto.response.workspace;

import com.bishamon.todo.dto.response.user.UserSummaryResponse;
import com.bishamon.todo.enumeration.ContextualRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WorkspaceMemberResponse {
    private Long id;
    private UserSummaryResponse user;
    private ContextualRole contextualRole;
    private LocalDateTime joinedAt;

}
