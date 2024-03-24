package com.tahrioussama.employeemanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.EmployeePresenceStatistics;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.repositories.EmployeePresenceStatisticsRepository;
import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.tahrioussama.employeemanagement.config.JsonUtils.objectMapper;

@Service
@AllArgsConstructor
public class EmployeePresenceServiceImpl implements EmployeePresenceService {

    private PresenceRepository presenceRepository;
    private EmployeeRepository employeeRepository;
    private EmployeePresenceStatisticsRepository employeePresenceStatisticsRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeePresenceServiceImpl.class);
    @Override
    public void updatePresenceValueForDate(Long employeeId, LocalDate providedDate, boolean newValue) {
        // Find the employee by ID
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();

            // Find the presence entities for the specified employee on the provided date
            List<Presence> presenceList = presenceRepository.findByEmployeeAndDate(employee, providedDate);

            // Update the presence value for each presence entity found
            for (Presence presence : presenceList) {
                // Update the presence value
                presence.setPresent(newValue);
                presenceRepository.save(presence);
            }

            // After updating presence, update the presence statistics for the employee
            updatePresenceStatisticsForEmployee(employee);
        } else {
            // Log and throw exception if employee is not found
            LOGGER.error("Employee with ID {} not found.", employeeId);
            throw new IllegalArgumentException("Employee with ID " + employeeId + " not found.");
        }
    }

    // Method to update presence statistics for a specific employee
    private void updatePresenceStatisticsForEmployee(Employee employee) {
        // Calculate and save presence statistics for the given employee
        Map<String, Object> employeeStats = calculateEmployeePresenceStatistics(employee);

        // Find existing EmployeePresenceStatistics entity or create a new one
        Optional<EmployeePresenceStatistics> optionalStatistics = employeePresenceStatisticsRepository.findByEmployee(employee);
        EmployeePresenceStatistics presenceStatistics = optionalStatistics.orElse(new EmployeePresenceStatistics());

        // Update entity fields
        presenceStatistics.setEmployee(employee);
        presenceStatistics.setStatistics(mapToJsonString(employeeStats));

        // Save the updated entity
        employeePresenceStatisticsRepository.save(presenceStatistics);
    }

    // Helper method to calculate employee presence statistics
    private Map<String, Object> calculateEmployeePresenceStatistics(Employee employee) {
        Map<String, Object> employeeStats = new HashMap<>();

        // Calculate weekly presence
        Map<String, Integer> weeklyPresence = new HashMap<>();
        LocalDate startDate = LocalDate.of(2024, Month.MARCH, 1);
        for (int i = 0; i < 4; i++) {
            int count = (int) presenceRepository.countByEmployeeAndDateBetweenAndPresent(
                    employee,
                    startDate.with(DayOfWeek.MONDAY),
                    startDate.with(DayOfWeek.FRIDAY),
                    true
            );
            weeklyPresence.put("Week " + (i + 1), count);
            startDate = startDate.plusWeeks(1);
        }

        // Calculate monthly presence
        List<String> daysOfMonth = presenceRepository.findByEmployeeAndPresent(
                        employee, true).stream()
                .map(p -> p.getDate().getDayOfWeek().toString())
                .distinct()
                .collect(Collectors.toList());

        // Store presence statistics for employee
        employeeStats.put("employeeName", employee.getResourceName());
        employeeStats.put("weeklyPresence", weeklyPresence);
        employeeStats.put("monthlyPresence", daysOfMonth);

        return employeeStats;
    }

    // Helper method to convert map to JSON string
    private String mapToJsonString(Map<String, Object> map) {
        try {
            // Use ObjectMapper to convert map to JSON string
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}