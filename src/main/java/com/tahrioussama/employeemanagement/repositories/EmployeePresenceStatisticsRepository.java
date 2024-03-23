package com.tahrioussama.employeemanagement.repositories;

import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.EmployeePresenceStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeePresenceStatisticsRepository extends JpaRepository<EmployeePresenceStatistics,Long> {
    Optional<EmployeePresenceStatistics> findByEmployee(Employee employee);
}
