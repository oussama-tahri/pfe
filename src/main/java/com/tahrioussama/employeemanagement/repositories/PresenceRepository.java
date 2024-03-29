package com.tahrioussama.employeemanagement.repositories;

import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PresenceRepository extends JpaRepository<Presence,Long> {
    List<Presence> findByEmployee(Employee employee);

    // Define a method to count presence records for a specific employee within a date range
    long countByEmployeeAndDateBetweenAndPresent(Employee employee, LocalDate startDate, LocalDate endDate, boolean present);

    // Define a method to find presence records for a specific employee with a specific presence status
    List<Presence> findByEmployeeAndPresent(Employee employee, boolean present);

    @Query("SELECT COUNT(DISTINCT p.employee) FROM Presence p WHERE p.employee.squad = :squadName AND p.date >= :weekStartDate AND p.date < :weekEndDate")
    long countDistinctEmployeesBySquadAndWeekStartDate(@Param("squadName") String squadName, @Param("weekStartDate") LocalDate weekStartDate, @Param("weekEndDate") LocalDate weekEndDate);

    @Query("SELECT COUNT(DISTINCT p.employee) FROM Presence p WHERE p.employee.squad = :squadName AND MONTH(p.date) = MONTH(:monthStartDate) AND YEAR(p.date) = YEAR(:monthStartDate)")
    long countDistinctEmployeesBySquadAndMonthStartDate(@Param("squadName") String squadName, @Param("monthStartDate") LocalDate monthStartDate);

    boolean existsByEmployeeAndDateAndPresent(Employee employee, LocalDate date, boolean present);

    List<Presence> findByEmployeeAndDate(Employee employee, LocalDate date);
}
