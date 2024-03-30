package com.tahrioussama.employeemanagement.controller;

import com.tahrioussama.employeemanagement.enums.PresenceStatus;
import com.tahrioussama.employeemanagement.services.EmployeePresenceService;
import com.tahrioussama.employeemanagement.exceptions.EmployeeNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/presence")
@CrossOrigin("*")
@AllArgsConstructor
public class EmployeePresenceController {

    private final EmployeePresenceService presenceService;

    @PutMapping("/{employeeId}")
    public ResponseEntity<String> updatePresenceValue(
            @PathVariable Long employeeId,
            @RequestParam LocalDate date,
            @RequestParam PresenceStatus newValue
    ) {
        try {
            presenceService.updatePresenceValueForDate(employeeId, date, newValue);
            return ResponseEntity.ok("Presence value updated successfully");
        } catch (EmployeeNotFoundException e) {
            // Handle employee not found exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // Handle invalid input arguments
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handle other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleEmployeeNotFoundException(EmployeeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}