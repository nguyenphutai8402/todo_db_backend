package com.bishamon.todo.dto.request.workspace;

import com.bishamon.todo.enumeration.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWorkspaceRequest {
    @NotBlank(message = "Workspace name is required")
    @Size(max = 150, message = "Workspace name must not exceed 150 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private Visibility visibility;
}
