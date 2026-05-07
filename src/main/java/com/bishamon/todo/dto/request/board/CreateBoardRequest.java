package com.bishamon.todo.dto.request.board;

import com.bishamon.todo.enumeration.BoardVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateBoardRequest {
    @NotBlank(message = "Workspace name is required")
    @Size(max = 150, message = "Workspace name must not exceed 150 characters")
    String name;

    BoardVisibility visibility;
}
