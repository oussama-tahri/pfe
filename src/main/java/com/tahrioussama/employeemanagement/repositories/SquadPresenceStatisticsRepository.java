package com.tahrioussama.employeemanagement.repositories;

import com.tahrioussama.employeemanagement.entities.EmployeePresenceStatistics;
import com.tahrioussama.employeemanagement.entities.SquadPresenceStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SquadPresenceStatisticsRepository extends JpaRepository<SquadPresenceStatistics,Long> {
}
