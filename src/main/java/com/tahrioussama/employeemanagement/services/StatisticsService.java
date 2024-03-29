package com.tahrioussama.employeemanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsService {

    List<Employee> getAllEmployees();
    List<Presence> getPresence();
    void calculateAndSaveEmployeePresenceStatistics() throws JsonProcessingException;
    void calculateAndSaveSquadPresenceStatistics() throws JsonProcessingException;
    double calculateSquadPresencePercentagePerWeek(String squadName, LocalDate weekStartDate);
    double calculateSquadPresencePercentagePerMonth(String squadName, LocalDate monthStartDate);

    String calculateEmployeePresenceStatus(String employeeName);
}

