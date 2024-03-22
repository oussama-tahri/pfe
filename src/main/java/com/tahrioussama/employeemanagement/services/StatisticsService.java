package com.tahrioussama.employeemanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;

public interface StatisticsService {
    void calculateAndSaveEmployeePresenceStatistics() throws JsonProcessingException;
    void calculateAndSaveSquadPresenceStatistics() throws JsonProcessingException;
    double calculateSquadPresencePercentagePerWeek(String squadName, LocalDate weekStartDate);
    double calculateSquadPresencePercentagePerMonth(String squadName, LocalDate monthStartDate);

    String calculateEmployeePresenceStatus(String employeeName);
}

