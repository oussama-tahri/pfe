package com.tahrioussama.employeemanagement.repositories;

import com.tahrioussama.employeemanagement.entities.SquadPresenceStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SquadPresenceStatisticsRepository extends JpaRepository<SquadPresenceStatistics,Long> {
    Optional<SquadPresenceStatistics> findBySquad(String squad);
}
