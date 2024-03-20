package com.tahrioussama.employeemanagement.services;


import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.EmployeePresenceStatistics;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.entities.SquadPresenceStatistics;
import com.tahrioussama.employeemanagement.repositories.EmployeePresenceStatisticsRepository;
import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import com.tahrioussama.employeemanagement.repositories.SquadPresenceStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeePresenceStatisticsRepository employeePresenceStatisticsRepository;

    @Autowired
    private SquadPresenceStatisticsRepository squadPresenceStatisticsRepository;

    @Override
    public void calculateAndSaveEmployeePresenceStatistics() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            Map<String, Object> employeeStats = calculateEmployeePresenceStatistics(employee);

            // Create and save EmployeePresenceStatistics entity
            EmployeePresenceStatistics presenceStatistics = new EmployeePresenceStatistics();
            presenceStatistics.setEmployee(employee);
            presenceStatistics.setStatistics(mapToJsonString(employeeStats));

            employeePresenceStatisticsRepository.save(presenceStatistics);
        }
    }

    @Override
    public void calculateAndSaveSquadPresenceStatistics() {
        List<Presence> allPresences = presenceRepository.findAll();
        Map<String, Map<String, Object>> squadStatistics = new HashMap<>();

        for (Presence presence : allPresences) {
            String squad = presence.getEmployee().getSquad();
            Map<String, Object> squadStats = squadStatistics.getOrDefault(squad, new HashMap<>());

            // Calculate weekly presence
            Map<String, Integer> weeklyPresence = squadStats.containsKey("weeklyPresence") ?
                    (Map<String, Integer>) squadStats.get("weeklyPresence") :
                    new HashMap<>();
            LocalDate startDate = LocalDate.of(2024, Month.MARCH, 1);
            for (int i = 0; i < 4; i++) {
                int count = (int) presenceRepository.countByEmployeeAndDateBetweenAndPresent(
                        presence.getEmployee(),
                        startDate.with(DayOfWeek.MONDAY),
                        startDate.with(DayOfWeek.FRIDAY),
                        "1"
                );
                weeklyPresence.put("Week " + (i + 1), count);
                startDate = startDate.plusWeeks(1);
            }
            squadStats.put("weeklyPresence", weeklyPresence);

            // Calculate monthly presence
            Map<String, List<String>> monthlyPresence = squadStats.containsKey("monthlyPresence") ?
                    (Map<String, List<String>>) squadStats.get("monthlyPresence") :
                    new HashMap<>();
            List<String> daysOfMonth = presenceRepository.findByEmployeeAndPresent(
                            presence.getEmployee(), "1").stream()
                    .map(p -> p.getDate().getDayOfWeek().toString())
                    .distinct()
                    .collect(Collectors.toList());
            monthlyPresence.put("Month", daysOfMonth);
            squadStats.put("monthlyPresence", monthlyPresence);

            squadStatistics.put(squad, squadStats);
        }

        // Create and save SquadPresenceStatistics entities
        for (Map.Entry<String, Map<String, Object>> entry : squadStatistics.entrySet()) {
            SquadPresenceStatistics presenceStatistics = new SquadPresenceStatistics();
            presenceStatistics.setSquad(entry.getKey());
            presenceStatistics.setStatistics(mapToJsonString(entry.getValue()));

            squadPresenceStatisticsRepository.save(presenceStatistics);
        }
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
                    "1"
            );
            weeklyPresence.put("Week " + (i + 1), count);
            startDate = startDate.plusWeeks(1);
        }

        // Calculate monthly presence
        List<String> daysOfMonth = presenceRepository.findByEmployeeAndPresent(
                        employee, "1").stream()
                .map(p -> p.getDate().getDayOfWeek().toString())
                .distinct()
                .collect(Collectors.toList());

        // Store presence statistics for employee
        employeeStats.put("employeeName", employee.getResourceName());
        employeeStats.put("weeklyPresence", weeklyPresence);
        employeeStats.put("monthlyPresence", daysOfMonth);

        return employeeStats;
    }

    // Helper method to convert map to JSON string (simplified)
    private String mapToJsonString(Map<String, Object> map) {
        return map.toString();
    }
}