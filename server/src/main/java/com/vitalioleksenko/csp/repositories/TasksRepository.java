package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TasksRepository extends JpaRepository<Task, Integer> {
    @Query("SELECT t FROM Task t JOIN t.group g JOIN g.members m WHERE m.user.userId = :userId")
    List<Task> findByUserId(@Param("userId")int userId);

    List<Task> findByGroup_GroupId(int groupId);

    @Query("SELECT t FROM Task t JOIN t.group g JOIN g.members m WHERE g.groupId = :groupId AND m.user.userId = :userId")
    List<Task> findByUserIdAndGroupId(@Param("userId")int userId, @Param("groupId")int groupId);
}
