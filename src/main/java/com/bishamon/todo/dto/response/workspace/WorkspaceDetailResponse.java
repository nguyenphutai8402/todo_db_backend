package com.bishamon.todo.dto.response.workspace;

import com.bishamon.todo.dto.response.user.UserSummaryResponse;
import com.bishamon.todo.dto.response.workspace_member.WorkspaceMemberResponse;
import com.bishamon.todo.dto.response.workspace_member.WorkspaceMemberSummaryResponse;
import com.bishamon.todo.enumeration.WorkspaceVisibility;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

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
    WorkspaceVisibility workspaceVisibility;
    UserSummaryResponse createdBy;
    Set<WorkspaceMemberSummaryResponse> members;
    LocalDateTime createdAt;
    LocalDateTime updateAt;
    int totalMember;
//    int totalBoard;
}

