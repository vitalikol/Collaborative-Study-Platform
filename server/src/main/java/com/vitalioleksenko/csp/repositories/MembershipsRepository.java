package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MembershipsRepository extends JpaRepository<Membership, Integer> {
    @Query("""
        SELECT m.user.userId
        FROM Membership m
        WHERE m.group.groupId = :groupId
    """)
    List<Integer> findUserIdsByGroupId(@Param("groupId") int groupId);
}

