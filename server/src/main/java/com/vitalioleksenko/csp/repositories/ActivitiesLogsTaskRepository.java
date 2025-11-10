package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivitiesLogsTaskRepository extends JpaRepository<ActivityLog, Integer> {
}
