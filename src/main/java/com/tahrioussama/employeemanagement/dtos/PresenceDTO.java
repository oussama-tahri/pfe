package com.tahrioussama.employeemanagement.dtos;

import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.enums.PresenceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PresenceDTO {
    private Long id;
    private LocalDate date;
    private PresenceStatus present;
    private EmployeeDTO employee;
    private String employeeName;

    public PresenceDTO(Long id, LocalDate date, PresenceStatus present, EmployeeDTO employee) {
        this.id = id;
        this.date = date;
        this.present = present;
        this.employee = employee;
    }
}
