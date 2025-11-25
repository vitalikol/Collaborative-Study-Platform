package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.util.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TasksRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {
    @Query("""
        SELECT COUNT(t)
        FROM Membership m
        JOIN m.group g
        JOIN g.tasks t
        WHERE m.user.userId = :userId
          AND t.status = :status
    """)
    int countTasksByStatus(@Param("userId") int userId, @Param("status") TaskStatus status);
}
