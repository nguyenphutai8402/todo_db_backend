package com.bishamon.todo.dto.request.workspace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UpdateWorkspaceRequest {
    @NotBlank(message = "Workspace name is required")
    @Size(max = 150, message = "Workspace name must not exceed 150 characters")
    String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description;
}
