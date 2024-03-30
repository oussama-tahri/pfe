package com.tahrioussama.employeemanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tahrioussama.employeemanagement.enums.PresenceStatus;
import com.tahrioussama.employeemanagement.exceptions.EmployeeNotFoundException;

import java.time.LocalDate;

public interface EmployeePresenceService {
    void updatePresenceValueForDate(Long employeeId, LocalDate date, PresenceStatus newValue) throws JsonProcessingException, EmployeeNotFoundException;
}
