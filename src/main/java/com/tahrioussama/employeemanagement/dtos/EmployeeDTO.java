package com.tahrioussama.employeemanagement.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmployeeDTO {
    private Long id;
    private String resourceName;
    private String site;
    private String tribe;
    private String squad;
    private String commentaire;
}
