package com.bishamon.todo.dto.response.workspace;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class WorkspaceSummaryResponse {
    Long id;
    String name;
    String logoUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
