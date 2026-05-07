package com.bishamon.todo.entity;

import com.bishamon.todo.enumeration.WorkspaceVisibility;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "workspaces",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "createdBy"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Workspace extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false,
            columnDefinition = "varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin")
    private String name;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "logo_url", columnDefinition = "TEXT")
    String logoUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    WorkspaceVisibility workspaceVisibility = WorkspaceVisibility.PRIVATE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    User createdBy;
    //    Set<>
    @Builder.Default
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<WorkspaceMember> members = new HashSet<>();

    public void addMember(WorkspaceMember member) {
        member.setWorkspace(this);
        this.members.add(member);
    }
}
