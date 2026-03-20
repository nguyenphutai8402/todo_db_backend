package com.bishamon.todo.repository;

import com.bishamon.todo.entity.User;
import com.bishamon.todo.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    boolean existsByCreatedByAndNameIgnoreCase(User createdBy, String name);
    @Query("""
                select w from Workspace w 
                join WorkspaceMember wm on w.id = wm.workspace.id
                where wm.user.id = :userId
            """)
    List<Workspace> findAllByUserId(@Param("userId") Long userId);
}
