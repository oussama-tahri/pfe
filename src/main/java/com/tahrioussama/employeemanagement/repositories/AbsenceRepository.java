package com.tahrioussama.employeemanagement.repositories;

import com.tahrioussama.employeemanagement.entities.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsenceRepository extends JpaRepository<Absence,Long> {
}
