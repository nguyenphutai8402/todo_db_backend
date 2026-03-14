package com.bishamon.todo.entity;

import com.bishamon.todo.enumeration.ContextualRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "workspace_members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"workspace_id", "user_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkspaceMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "contextual_role", nullable = false, length = 20)
    ContextualRole contextualRole = ContextualRole.MEMBER;

    @Column(name = "joined_at", nullable = false)
    LocalDateTime joinedAt;
}
