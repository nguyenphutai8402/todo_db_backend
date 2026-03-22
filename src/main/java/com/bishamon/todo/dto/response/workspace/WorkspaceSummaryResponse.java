package com.bishamon.todo.dto.response.workspace;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
}
