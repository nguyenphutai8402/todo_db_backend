package com.bishamon.todo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "card_members",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"card_id", "user_id"})}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "added_at")
    private LocalDateTime addedAt;
}
