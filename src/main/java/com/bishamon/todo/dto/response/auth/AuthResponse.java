package com.bishamon.todo.dto.response.auth;

import com.bishamon.todo.enumeration.GlobalRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AuthResponse {
    String accessToken;
    @Builder.Default
    String tokenType = "Bearer";
    Long id;
    String email;
    String fullName;
    GlobalRole globalRole;
}
