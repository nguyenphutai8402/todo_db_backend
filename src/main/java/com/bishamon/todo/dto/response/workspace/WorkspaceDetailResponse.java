package com.bishamon.todo.dto.response.workspace;

import com.bishamon.todo.dto.response.user.UserSummaryResponse;
import com.bishamon.todo.enumeration.Visibility;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceDetailResponse {
    private Long id;
    private String name;
    private String description;
    private Visibility visibility;
    private Long createdBy;
    private List<UserSummaryResponse> members;
    private int memberCount;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}

