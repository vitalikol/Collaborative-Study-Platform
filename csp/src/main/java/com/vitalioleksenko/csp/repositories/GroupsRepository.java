package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupsRepository extends JpaRepository<Group, Integer> {
}
