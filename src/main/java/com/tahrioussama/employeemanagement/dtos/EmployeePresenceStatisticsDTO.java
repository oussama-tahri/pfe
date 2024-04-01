package com.tahrioussama.employeemanagement.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmployeePresenceStatisticsDTO {
    private Long id;
    private EmployeeDTO employee;
    private String statistics;
}
