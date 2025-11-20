package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourcesRepository extends JpaRepository<Resource, Integer> {
    Page<Resource> findByGroup_GroupId(int groupGroupId, Pageable pageable);
}
