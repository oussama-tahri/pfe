package com.tahrioussama.employeemanagement.repositories;

import com.tahrioussama.employeemanagement.entities.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface CalendarRepository extends JpaRepository<Calendar,Long> {
    // Recherche une entrée dans la table calendars en fonction de la date de début et de la date de fin de la semaine
    Calendar findByWeekStartDateAndWeekEndDate(Date weekStartDate, Date weekEndDate);
}
