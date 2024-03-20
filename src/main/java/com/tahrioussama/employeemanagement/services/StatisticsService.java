package com.tahrioussama.employeemanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface StatisticsService {
    void calculateAndSaveEmployeePresenceStatistics() throws JsonProcessingException;
    void calculateAndSaveSquadPresenceStatistics() throws JsonProcessingException;
}

