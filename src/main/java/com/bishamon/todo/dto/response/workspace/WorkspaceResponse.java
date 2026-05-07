package com.bishamon.todo.dto.response.workspace;

import com.bishamon.todo.dto.response.user.UserSummaryResponse;
import com.bishamon.todo.enumeration.WorkspaceVisibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkspaceResponse {
    Long id;
    String name;
    String description;
    String logoUrl;
    WorkspaceVisibility workspaceVisibility;
    UserSummaryResponse createdBy;
    LocalDateTime createdAt;
    LocalDateTime updateAt;
}
