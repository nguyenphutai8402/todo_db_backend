package com.bishamon.todo.dto.response.workspace;

import com.bishamon.todo.dto.response.user.UserSummaryResponse;
import com.bishamon.todo.enumeration.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateWorkspaceResponse {
    private Long id;
    private String name;
    private String description;
    private Visibility visibility;
    private UserSummaryResponse createdBy;
    private int memberCount;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}

