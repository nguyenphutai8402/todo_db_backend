package com.bishamon.todo.dto.response.workspace;

import com.bishamon.todo.dto.response.user.UserSummaryResponse;
import com.bishamon.todo.enumeration.Visibility;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkspaceDetailResponse {
    Long id;
    String name;
    String description;
    String logoUrl;
    Visibility visibility;
    UserSummaryResponse createdBy;
    int memberCount;
    LocalDateTime createdAt;
    LocalDateTime updateAt;
}

