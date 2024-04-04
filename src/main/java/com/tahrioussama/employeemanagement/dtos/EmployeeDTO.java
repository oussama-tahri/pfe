package com.tahrioussama.employeemanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String resourceName;
    private String site;
    private String tribe;
    private String squad;
    private String commentaire;
}
