package com.bishamon.todo.entity;

import com.bishamon.todo.enumeration.ContextualRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "board_members",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"board_id", "user_id"})}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "contextual_role", nullable = false, length = 20)
    private ContextualRole contextualRole = ContextualRole.MEMBER;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}
