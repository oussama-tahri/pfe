package com.tahrioussama.employeemanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.exceptions.EmployeeNotFoundException;
import com.tahrioussama.employeemanagement.exceptions.NoSquadAssignedException;
import com.tahrioussama.employeemanagement.exceptions.PresenceStatisticsNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsService {

    List<Employee> getAllEmployees();
    List<Presence> getPresence();
    void calculateAndSaveEmployeePresenceStatistics() throws JsonProcessingException;
    void calculateAndSaveSquadPresenceStatistics() throws JsonProcessingException;
    double calculateSquadPresencePercentagePerWeek(String squadName, LocalDate weekStartDate);
    double calculateSquadPresencePercentagePerMonth(String squadName, LocalDate monthStartDate);

    String calculateEmployeePresenceStatus(String employeeName) throws PresenceStatisticsNotFoundException, NoSquadAssignedException, EmployeeNotFoundException;
}

