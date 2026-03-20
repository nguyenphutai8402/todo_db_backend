package com.bishamon.todo.dto.response.user;

import com.bishamon.todo.enumeration.GlobalRole;
import com.bishamon.todo.enumeration.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSummaryResponse {
    private Long id;
    private String email;
    private String fullName;
    private String avatarUrl;
    private UserStatus status;
    private GlobalRole globalRole;
}
