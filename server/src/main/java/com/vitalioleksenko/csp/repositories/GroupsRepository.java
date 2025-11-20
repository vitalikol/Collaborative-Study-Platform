package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupsRepository extends JpaRepository<Group, Integer> {
    @Query("SELECT g FROM Group g JOIN g.members m WHERE m.user.userId = :userId")
    Page<Group> findAllByUserId(@Param("userId") int userId, Pageable pageable);
}
