package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TasksRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUser_UserId(int userUserId);
}
