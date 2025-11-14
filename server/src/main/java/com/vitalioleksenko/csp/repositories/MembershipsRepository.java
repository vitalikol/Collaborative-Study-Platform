package com.vitalioleksenko.csp.repositories;

import com.vitalioleksenko.csp.models.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipsRepository extends JpaRepository<Membership, Integer> {
}
