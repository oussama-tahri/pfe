package com.tahrioussama.employeemanagement.controller;

import com.tahrioussama.employeemanagement.dtos.EmployeeDTO;
import com.tahrioussama.employeemanagement.dtos.PresenceDTO;
import com.tahrioussama.employeemanagement.services.StatisticsService;
import com.tahrioussama.employeemanagement.exceptions.PresenceStatisticsNotFoundException;
import com.tahrioussama.employeemanagement.exceptions.NoSquadAssignedException;
import com.tahrioussama.employeemanagement.exceptions.EmployeeNotFoundException;
import lombok.AllArgsConstructor;
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

    private final StatisticsService statisticsService;

    @GetMapping("/getEmployees")
    public List<EmployeeDTO> getAllEmployees() {
        return statisticsService.getAllEmployees();
    }

    @GetMapping("/getPresence")
    public List<PresenceDTO> getPresence() {
        return statisticsService.getPresence();
    }

    @PostMapping("/employees")
    public ResponseEntity<String> calculateAndSaveEmployeePresenceStatistics() {
        try {
            statisticsService.calculateAndSaveEmployeePresenceStatistics();
            return ResponseEntity.status(HttpStatus.OK).body("Employee presence statistics calculated and saved successfully.");
        } catch (Exception e) {
            // Log the exception
            // logger.error("An error occurred while calculating and saving employee presence statistics.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while calculating and saving employee presence statistics.");
        }
    }

    @PostMapping("/squads")
    public ResponseEntity<String> calculateAndSaveSquadPresenceStatistics() {
        try {
            statisticsService.calculateAndSaveSquadPresenceStatistics();
            return ResponseEntity.status(HttpStatus.OK).body("Squad presence statistics calculated and saved successfully.");
        } catch (Exception e) {
            // Log the exception
            // logger.error("An error occurred while calculating and saving squad presence statistics.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while calculating and saving squad presence statistics.");
        }
    }

    @GetMapping("/presence-per-week/{squadName}/{weekStartDate}")
    public ResponseEntity<Double> getSquadPresencePercentagePerWeek(
            @PathVariable String squadName,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate
    ) {
        try {
            double percentage = statisticsService.calculateSquadPresencePercentagePerWeek(squadName, weekStartDate);
            return ResponseEntity.ok(percentage);
        } catch (Exception e) {
            // Log the exception
            // logger.error("An error occurred while calculating squad presence percentage per week.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Double.valueOf("An error occurred while calculating squad presence percentage per week."));
        }
    }

    @GetMapping("/presence-per-month/{squadName}/{monthStartDate}")
    public ResponseEntity<Double> getSquadPresencePercentagePerMonth(
            @PathVariable String squadName,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate monthStartDate
    ) {
        try {
            double percentage = statisticsService.calculateSquadPresencePercentagePerMonth(squadName, monthStartDate);
            return ResponseEntity.ok(percentage);
        } catch (Exception e) {
            // Log the exception
            // logger.error("An error occurred while calculating squad presence percentage per month.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Double.valueOf("An error occurred while calculating squad presence percentage per month."));
        }
    }

    @GetMapping("/presence-status")
    public ResponseEntity<String> getEmployeePresenceStatus(@RequestParam String employeeName) {
        try {
            String presenceStatus = statisticsService.calculateEmployeePresenceStatus(employeeName);
            return ResponseEntity.ok(presenceStatus);
        } catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NoSquadAssignedException | PresenceStatisticsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Log the exception
            // logger.error("An error occurred while calculating employee presence status.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while calculating employee presence status.");
        }
    }
}