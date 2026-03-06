package com.bishamon.todo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "lists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private Integer position;

    @Column(name = "is_archived")
    private boolean isArchived;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
    private List<Card> cards;
}
