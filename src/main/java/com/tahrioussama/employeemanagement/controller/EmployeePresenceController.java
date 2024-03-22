package com.tahrioussama.employeemanagement.controller;

import com.tahrioussama.employeemanagement.services.EmployeePresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/presence")
public class EmployeePresenceController {

    @Autowired
    private EmployeePresenceService presenceService;

    @PutMapping("/{presenceId}")
    public ResponseEntity<String> updatePresenceValue(
            @PathVariable Long presenceId,
            @RequestParam LocalDate date,
            @RequestParam boolean newValue
    ) {
        try {
            presenceService.updatePresenceValueForDate(presenceId, date, newValue);
            return ResponseEntity.ok("Presence value updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}
