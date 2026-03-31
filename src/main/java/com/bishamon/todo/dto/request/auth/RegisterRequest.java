package com.bishamon.todo.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
     @Email(message = "Invalid email")
     @NotBlank(message = "Email is required")
     @Size(max = 100, message = "Email must not exceed 100 characters")
     String email;

     @NotBlank(message = "Password is required")
     String password;

     @NotBlank(message = "Full Name is required")
     @Size(max = 100, message = "Full name must not exceed 100 characters")
     String fullName;
}
