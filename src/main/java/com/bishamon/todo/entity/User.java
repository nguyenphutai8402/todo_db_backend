package com.bishamon.todo.entity;

import com.bishamon.todo.enumeration.GlobalRole;
import com.bishamon.todo.enumeration.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String email;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    String avatarUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    UserStatus status = UserStatus.ACTIVE;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    GlobalRole globalRole = GlobalRole.USER;
}
