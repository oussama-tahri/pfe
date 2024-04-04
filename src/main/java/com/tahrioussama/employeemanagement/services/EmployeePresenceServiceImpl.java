package com.tahrioussama.employeemanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tahrioussama.employeemanagement.dtos.EmployeePresenceStatisticsDTO;
import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.enums.PresenceStatus;
import com.tahrioussama.employeemanagement.exceptions.EmployeeNotFoundException;
import com.tahrioussama.employeemanagement.mappers.EmployeeMapper;
import com.tahrioussama.employeemanagement.mappers.EmployeePresenceStatisticsMapper;
import com.tahrioussama.employeemanagement.repositories.EmployeePresenceStatisticsRepository;
import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tahrioussama.employeemanagement.config.JsonUtils.objectMapper;

@Service
@AllArgsConstructor
public class EmployeePresenceServiceImpl implements EmployeePresenceService {

    private final PresenceRepository presenceRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeePresenceStatisticsRepository employeePresenceStatisticsRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeePresenceServiceImpl.class);

    private final EmployeePresenceStatisticsMapper presenceStatisticsMapper;
    private final EmployeeMapper employeeMapper;

    @Override
    public void updatePresenceValueForDate(Long employeeId, LocalDate providedDate, PresenceStatus newValue) throws EmployeeNotFoundException {
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
            // Log and throw custom exception if employee is not found
            LOGGER.error("Employee with ID {} not found.", employeeId);
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found.");
        }
    }

    // Method to update presence statistics for a specific employee
    private void updatePresenceStatisticsForEmployee(Employee employee) {
        // Calculate and save presence statistics for the given employee
        Map<String, Object> employeeStats = calculateEmployeePresenceStatistics(employee);

        // Convert the map to DTO
        EmployeePresenceStatisticsDTO presenceStatisticsDTO = new EmployeePresenceStatisticsDTO();
        presenceStatisticsDTO.setEmployee(employeeMapper.employeeToDTO(employee));
        presenceStatisticsDTO.setStatistics(mapToJsonString(employeeStats));

        // Save the updated entity
        employeePresenceStatisticsRepository.save(presenceStatisticsMapper.dtoToPresenceStatistics(presenceStatisticsDTO));
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
                    PresenceStatus.PRESENT // Use PresenceStatus enum instead of boolean
            );
            weeklyPresence.put("Week " + (i + 1), count);
            startDate = startDate.plusWeeks(1);
        }

        // Calculate monthly presence
        List<String> daysOfMonth = presenceRepository.findByEmployeeAndPresent(
                        employee, PresenceStatus.PRESENT).stream() // Use PresenceStatus enum instead of boolean
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
    private String mapToJsonString(Object object) {
        try {
            // Use ObjectMapper to convert object to JSON string
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // Log and throw custom exception for JSON processing failure
            LOGGER.error("Error processing JSON: {}", e.getMessage());
            throw new RuntimeException("Error processing JSON");
        }
    }
}