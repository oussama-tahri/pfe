package com.tahrioussama.employeemanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tahrioussama.employeemanagement.dtos.EmployeeDTO;
import com.tahrioussama.employeemanagement.dtos.EmployeePresenceStatisticsDTO;
import com.tahrioussama.employeemanagement.dtos.PresenceDTO;
import com.tahrioussama.employeemanagement.dtos.SquadPresenceStatisticsDTO;
import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.entities.SquadPresenceStatistics;
import com.tahrioussama.employeemanagement.enums.PresenceStatus;
import com.tahrioussama.employeemanagement.exceptions.EmployeeNotFoundException;
import com.tahrioussama.employeemanagement.exceptions.NoSquadAssignedException;
import com.tahrioussama.employeemanagement.exceptions.PresenceStatisticsNotFoundException;
import com.tahrioussama.employeemanagement.mappers.EmployeeMapper;
import com.tahrioussama.employeemanagement.mappers.EmployeePresenceStatisticsMapper;
import com.tahrioussama.employeemanagement.mappers.PresenceMapper;
import com.tahrioussama.employeemanagement.mappers.SquadPresenceStatisticsMapper;
import com.tahrioussama.employeemanagement.repositories.EmployeePresenceStatisticsRepository;
import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import com.tahrioussama.employeemanagement.repositories.SquadPresenceStatisticsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static com.tahrioussama.employeemanagement.config.JsonUtils.jsonStringToMap;
import static com.tahrioussama.employeemanagement.config.JsonUtils.objectMapper;

@Service
@AllArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final PresenceRepository presenceRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeePresenceStatisticsRepository employeePresenceStatisticsRepository;
    private final SquadPresenceStatisticsRepository squadPresenceStatisticsRepository;
    private final EmployeeMapper employeeMapper;
    private final PresenceMapper presenceMapper;
    private final EmployeePresenceStatisticsMapper employeePresenceStatisticsMapper;
    private final SquadPresenceStatisticsMapper squadPresenceStatisticsMapper;

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employeeMapper::employeeToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PresenceDTO> getPresence() {
        List<Presence> presences = presenceRepository.findAll();
        return presences.stream()
                .map(presenceMapper::presenceToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void calculateAndSaveEmployeePresenceStatistics() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            Map<String, Object> employeeStats = calculateEmployeePresenceStatistics(employee);

            // Create and save EmployeePresenceStatistics entity
            EmployeePresenceStatisticsDTO presenceStatisticsDTO = new EmployeePresenceStatisticsDTO();
            presenceStatisticsDTO.setEmployee(employeeMapper.employeeToDTO(employee));
            presenceStatisticsDTO.setStatistics(mapToJsonString(employeeStats));

            employeePresenceStatisticsRepository.save(employeePresenceStatisticsMapper.dtoToPresenceStatistics(presenceStatisticsDTO));
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
                        PresenceStatus.PRESENT
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
                            presence.getEmployee(), PresenceStatus.PRESENT).stream()
                    .map(p -> p.getDate().getDayOfWeek().toString())
                    .distinct()
                    .collect(Collectors.toList());
            monthlyPresence.put("Month", daysOfMonth);
            squadStats.put("monthlyPresence", monthlyPresence);

            squadStatistics.put(squad, squadStats);
        }

        // Create and save SquadPresenceStatistics entities
        for (Map.Entry<String, Map<String, Object>> entry : squadStatistics.entrySet()) {
            SquadPresenceStatisticsDTO presenceStatisticsDTO = new SquadPresenceStatisticsDTO();
            presenceStatisticsDTO.setSquad(entry.getKey());
            presenceStatisticsDTO.setStatistics(mapToJsonString(entry.getValue()));

            squadPresenceStatisticsRepository.save(squadPresenceStatisticsMapper.dtoToPresenceStatistics(presenceStatisticsDTO));
        }
    }

    @Override
    public double calculateSquadPresencePercentagePerWeek(String squadName, LocalDate weekStartDate) {
        // Calculate the end date of the week
        LocalDate weekEndDate = weekStartDate.plusDays(7);

        // Calculate the number of employees present in the squad for the given week
        long presentEmployeesInSquad = presenceRepository.countDistinctEmployeesBySquadAndWeekStartDate(squadName, weekStartDate, weekEndDate);
        long totalEmployeesInSquad = employeeRepository.countBySquad(squadName);

        // Calculate the percentage
        return totalEmployeesInSquad == 0 ? 0 : (double) presentEmployeesInSquad / totalEmployeesInSquad * 100;
    }

    @Override
    public double calculateSquadPresencePercentagePerMonth(String squadName, LocalDate monthStartDate) {
        // Calculate the number of employees present in the squad for the given month
        long totalEmployeesInSquad = employeeRepository.countBySquad(squadName);
        long presentEmployeesInSquad = presenceRepository.countDistinctEmployeesBySquadAndMonthStartDate(squadName, monthStartDate);

        // Calculate the percentage
        return (double) presentEmployeesInSquad / totalEmployeesInSquad * 100;
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
                    PresenceStatus.PRESENT // Update to use PresenceStatus enum
            );
            weeklyPresence.put("Week " + (i + 1), count);
            startDate = startDate.plusWeeks(1);
        }

        // Calculate monthly presence
        List<String> daysOfMonth = presenceRepository.findByEmployeeAndPresent(
                        employee, PresenceStatus.PRESENT) // Update to use PresenceStatus enum
                .stream()
                .map(p -> p.getDate().getDayOfWeek().toString())
                .distinct()
                .collect(Collectors.toList());

        // Store presence statistics for employee
        employeeStats.put("employeeName", employee.getResourceName());
        employeeStats.put("weeklyPresence", weeklyPresence);
        employeeStats.put("monthlyPresence", daysOfMonth);

        return employeeStats;
    }

    @Override
    public String calculateEmployeePresenceStatus(String employeeName) throws PresenceStatisticsNotFoundException, NoSquadAssignedException, EmployeeNotFoundException {
        // Find the employee by name
        Optional<Employee> optionalEmployee = employeeRepository.findByResourceName(employeeName);
        if (optionalEmployee.isEmpty()) {
            throw new EmployeeNotFoundException("Employee with name " + employeeName + " not found.");
        }

        Employee employee = optionalEmployee.get();
        String squad = employee.getSquad();

        if (squad == null || squad.isEmpty()) {
            throw new NoSquadAssignedException("Employee " + employeeName + " has no squad assigned.");
        }

        // Retrieve the squad's presence statistics
        Optional<SquadPresenceStatistics> optionalStatistics = squadPresenceStatisticsRepository.findBySquad(squad);
        if (optionalStatistics.isEmpty()) {
            throw new PresenceStatisticsNotFoundException("No presence statistics found for squad " + squad + ".");
        }

        SquadPresenceStatistics statistics = optionalStatistics.get();
        Map<String, Object> squadStats = jsonStringToMap(statistics.getStatistics());

        // Calculate weekly presence
        List<String> weeklyPresence = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            String weekKey = "Week " + i;
            int squadPresenceCount = ((Map<String, Integer>) squadStats.getOrDefault("weeklyPresence", new HashMap<>()))
                    .getOrDefault(weekKey, 0);

            // Check if the employee is present in the same days as the squad (excluding weekends)
            if (squadPresenceCount == 0) {
                weeklyPresence.add("Absent in " + weekKey);
            } else {
                weeklyPresence.add("Present in " + weekKey);
            }
        }

        // Calculate monthly presence
        List<String> monthlyPresence = new ArrayList<>();
        List<String> daysOfMonth = ((Map<String, List<String>>) squadStats.getOrDefault("monthlyPresence", new HashMap<>()))
                .getOrDefault("Month", new ArrayList<>());

        // Check if the employee is present in the same days as the squad (excluding weekends)
        for (String day : daysOfMonth) {
            if (employeePresentOnDay(employee, day)) {
                monthlyPresence.add("Present on " + day);
            } else {
                monthlyPresence.add("Absent on " + day);
            }
        }

        // Build the presence status message
        StringBuilder presenceStatus = new StringBuilder();
        presenceStatus.append("Employee ").append(employeeName).append(" presence status:\n");
        presenceStatus.append("Weekly presence:\n");
        for (String weekPresence : weeklyPresence) {
            presenceStatus.append(weekPresence).append("\n");
        }
        presenceStatus.append("Monthly presence:\n");
        for (String monthPresence : monthlyPresence) {
            presenceStatus.append(monthPresence).append("\n");
        }

        return presenceStatus.toString();
    }

    // Helper method to check if an employee is present on a specific day (excluding weekends)
    private boolean employeePresentOnDay(Employee employee, String dayOfWeek) {
        if (dayOfWeek.equalsIgnoreCase("SATURDAY") || dayOfWeek.equalsIgnoreCase("SUNDAY")) {
            return false; // Exclude weekends
        }
        return presenceRepository.existsByEmployeeAndDateAndPresent(employee, dayOfWeekToDateTime(dayOfWeek), PresenceStatus.PRESENT);
    }

    // Helper method to convert day of week string to LocalDate
    private LocalDate dayOfWeekToDateTime(String dayOfWeek) {
        DayOfWeek day = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        return LocalDate.now().with(day);
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