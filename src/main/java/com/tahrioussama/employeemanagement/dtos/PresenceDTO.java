package com.tahrioussama.employeemanagement.dtos;

import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.enums.PresenceStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class PresenceDTO {
    private Long id;
    private LocalDate date;
    private PresenceStatus present;
    private EmployeeDTO employee;
    private String employeeName;
}
