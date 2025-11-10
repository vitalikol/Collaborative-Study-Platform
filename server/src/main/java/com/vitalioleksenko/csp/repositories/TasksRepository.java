package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Task, Integer> {
}
