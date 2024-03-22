package com.tahrioussama.employeemanagement.services;

import java.time.LocalDate;

public interface EmployeePresenceService {
    void updatePresenceValueForDate(Long presenceId, LocalDate date, boolean newValue);
}
