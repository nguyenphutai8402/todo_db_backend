package com.bishamon.todo.dto.request.workspace;


import com.bishamon.todo.enumeration.Visibility;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UpdateWorkspaceVisibilityRequest {
    @NotNull
    Visibility visibility;
}
