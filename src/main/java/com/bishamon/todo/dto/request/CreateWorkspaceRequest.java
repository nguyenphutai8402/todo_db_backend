package com.bishamon.todo.dto.request;

import com.bishamon.todo.enumeration.Visibility;
import lombok.Data;

@Data
public class CreateWorkspaceRequest {
    private String name;
    private String description;
    private Visibility visibility;
}
