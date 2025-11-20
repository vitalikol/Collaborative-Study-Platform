package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.ActivityLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivitiesLogsRepository extends JpaRepository<ActivityLog, Integer> {
    Page<ActivityLog> findByUser_UserId(int userUserId, Pageable pageable);
}
