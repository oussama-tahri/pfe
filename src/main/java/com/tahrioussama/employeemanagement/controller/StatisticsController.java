package com.tahrioussama.employeemanagement.controller;

import com.tahrioussama.employeemanagement.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

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
}
