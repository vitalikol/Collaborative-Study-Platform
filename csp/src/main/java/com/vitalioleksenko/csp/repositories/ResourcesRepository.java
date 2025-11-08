package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourcesRepository extends JpaRepository<Resource, Integer> {
}
