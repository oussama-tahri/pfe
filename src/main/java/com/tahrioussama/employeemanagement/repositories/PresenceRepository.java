package com.tahrioussama.employeemanagement.repositories;

import com.tahrioussama.employeemanagement.entities.Presence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresenceRepository extends JpaRepository<Presence,Long> {
}
