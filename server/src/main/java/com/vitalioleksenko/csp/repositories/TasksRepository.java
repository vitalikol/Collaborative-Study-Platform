package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TasksRepository extends JpaRepository<Task, Integer> {
    @Query("SELECT t FROM Task t JOIN t.group g JOIN g.members m WHERE m.user.userId = :userId")
    Page<Task> findByUserId(@Param("userId")int userId, Pageable pageable);

    Page<Task> findByGroup_GroupId(int groupId, Pageable pageable);

    @Query("SELECT t FROM Task t JOIN t.group g JOIN g.members m WHERE g.groupId = :groupId AND m.user.userId = :userId")
    Page<Task> findByUserIdAndGroupId(@Param("userId")int userId, @Param("groupId")int groupId, Pageable pageable);
}
