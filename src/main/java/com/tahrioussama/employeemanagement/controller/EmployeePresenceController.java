package com.tahrioussama.employeemanagement.controller;

import com.tahrioussama.employeemanagement.services.EmployeePresenceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/presence")
@CrossOrigin("*")
@AllArgsConstructor
public class EmployeePresenceController {

    private EmployeePresenceService presenceService;

    // POST http://localhost:8080/presence/107?date=2024-03-28&newValue=true
    @PutMapping("/{employeeId}")
    public ResponseEntity<String> updatePresenceValue(
            @PathVariable Long employeeId,
            @RequestParam LocalDate date,
            @RequestParam boolean newValue
    ) {
        try {
            presenceService.updatePresenceValueForDate(employeeId, date, newValue);
            return ResponseEntity.ok("Presence value updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}