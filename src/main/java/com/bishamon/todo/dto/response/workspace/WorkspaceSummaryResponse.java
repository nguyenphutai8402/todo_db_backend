package com.bishamon.todo.dto.response.workspace;

import com.bishamon.todo.dto.response.user.UserSummaryResponse;
import com.bishamon.todo.enumeration.Visibility;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class WorkspaceSummaryResponse {
    private Long id;
    private String name;
    private String description;
    private Visibility visibility;
    private UserSummaryResponse createdBy;
    private int memberCount;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}

