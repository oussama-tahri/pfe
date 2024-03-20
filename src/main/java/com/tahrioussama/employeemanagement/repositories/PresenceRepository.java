package com.tahrioussama.employeemanagement.repositories;

import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface PresenceRepository extends JpaRepository<Presence,Long> {
    List<Presence> findByEmployee(Employee employee);
    // Define a method to count presence records for a specific employee within a date range
    long countByEmployeeAndDateBetweenAndPresent(Employee employee, LocalDate startDate, LocalDate endDate, String present);

    // Define a method to find presence records for a specific employee with a specific presence status
    List<Presence> findByEmployeeAndPresent(Employee employee, String present);
}
