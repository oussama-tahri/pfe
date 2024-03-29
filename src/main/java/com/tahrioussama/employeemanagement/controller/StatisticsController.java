package com.tahrioussama.employeemanagement.controller;

import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import com.tahrioussama.employeemanagement.services.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@AllArgsConstructor
@CrossOrigin("*")
public class StatisticsController {

    private StatisticsService statisticsService;


    // GET http://localhost:8080/api/statistics/getEmployees
    @GetMapping("/getEmployees")
    public List<Employee> getAllEmployees() {
        return statisticsService.getAllEmployees();
    }


    // GET http://localhost:8080/api/statistics/getPresence
    @GetMapping("/getPresence")
    public List<Presence> getPresence() {
        return statisticsService.getPresence();
    }


    // POST http://localhost:8080/api/statistics/employees
    @PostMapping("/employees")
    public ResponseEntity<String> calculateAndSaveEmployeePresenceStatistics() {
        try {
            statisticsService.calculateAndSaveEmployeePresenceStatistics();
            return ResponseEntity.status(HttpStatus.OK).body("Employee presence statistics calculated and saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while calculating and saving employee presence statistics.");
        }
    }

    // POST http://localhost:8080/api/statistics/squads
    @PostMapping("/squads")
    public ResponseEntity<String> calculateAndSaveSquadPresenceStatistics() {
        try {
            statisticsService.calculateAndSaveSquadPresenceStatistics();
            return ResponseEntity.status(HttpStatus.OK).body("Squad presence statistics calculated and saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while calculating and saving squad presence statistics.");
        }
    }

    // GET http://localhost:8080/api/statistics/presence-per-week/CBE3/2024-03-01
    @GetMapping("/presence-per-week/{squadName}/{weekStartDate}")
    public ResponseEntity<Double> getSquadPresencePercentagePerWeek(
            @PathVariable String squadName,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate
    ) {
        double percentage = statisticsService.calculateSquadPresencePercentagePerWeek(squadName, weekStartDate);
        return new ResponseEntity<>(percentage, HttpStatus.OK);
    }

    // GET http://localhost:8080/api/statistics/presence-per-month/CBE3/2024-03-01
    @GetMapping("/presence-per-month/{squadName}/{monthStartDate}")
    public ResponseEntity<Double> getSquadPresencePercentagePerMonth(
            @PathVariable String squadName,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate monthStartDate
    ) {
        double percentage = statisticsService.calculateSquadPresencePercentagePerMonth(squadName, monthStartDate);
        return new ResponseEntity<>(percentage, HttpStatus.OK);
    }

    // GET http://localhost:8080/api/statistics/presence-status
    @GetMapping("/presence-status")
    public String getEmployeePresenceStatus(@RequestParam String employeeName) {
        return statisticsService.calculateEmployeePresenceStatus(employeeName);
    }
}