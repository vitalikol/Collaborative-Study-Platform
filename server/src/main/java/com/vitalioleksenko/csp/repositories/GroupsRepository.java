package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface GroupsRepository extends JpaRepository<Group, Integer> {
    Page<Group> findByMembersUserUserId(int userId, Pageable pageable);

    Page<Group> findByNameContainingIgnoreCase(String search, Pageable pageable);

    Page<Group> findByMembersUserUserIdAndNameContainingIgnoreCase(int userId, String search, Pageable pageable);
}
