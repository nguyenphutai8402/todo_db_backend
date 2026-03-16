package com.bishamon.todo.repository;

import com.bishamon.todo.entity.User;
import com.bishamon.todo.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    boolean existsByCreatedByAndNameIgnoreCase(User createdBy, String name);
}
