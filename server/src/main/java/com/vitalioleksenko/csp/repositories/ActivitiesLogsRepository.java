package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivitiesLogsRepository extends JpaRepository<ActivityLog, Integer> {
}
