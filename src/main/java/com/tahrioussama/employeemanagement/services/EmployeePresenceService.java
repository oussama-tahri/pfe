package com.tahrioussama.employeemanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;

public interface EmployeePresenceService {
    void updatePresenceValueForDate(Long employeeId, LocalDate date, boolean newValue) throws JsonProcessingException;
}
