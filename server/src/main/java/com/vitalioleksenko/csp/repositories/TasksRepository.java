package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.models.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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

    @Query("""
    SELECT t
    FROM Task t
    WHERE t.deadline BETWEEN :now AND :soon
    """)
    List<Task> findTasksWithDeadlineBetween(LocalDateTime now, LocalDateTime soon);
}
