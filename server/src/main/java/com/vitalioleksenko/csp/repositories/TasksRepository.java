package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.util.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TasksRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> { }
