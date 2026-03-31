package com.bishamon.todo.dto.response.workspace;

import com.bishamon.todo.dto.response.user.UserSummaryResponse;
import com.bishamon.todo.enumeration.Visibility;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class WorkspaceDetailResponse {
    Long id;
    String name;
    String description;
    String logoUrl;
    Visibility visibility;
    LocalDateTime createdAt;
    LocalDateTime updateAt;
}

