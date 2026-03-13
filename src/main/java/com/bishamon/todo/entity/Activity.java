package com.bishamon.todo.entity;

import com.bishamon.todo.enumeration.ActivityType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id", nullable = false)
    private User actor;

    @Enumerated(EnumType.STRING)
    private ActivityType action;

    @Column(name = "target_type")
    private String targetType; // Ví dụ: "card", "list", "comment"

    @Column(name = "target_id")
    private Long targetId; // ID của đối tượng bị tác động

    @Column(columnDefinition = "TEXT")
    private String data; // JSON chứa thông tin chi tiết (ví dụ: tên cũ -> tên mới)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
