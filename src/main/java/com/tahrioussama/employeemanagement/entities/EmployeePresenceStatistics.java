package com.tahrioussama.employeemanagement.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeePresenceStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Employee employee;

    @Column(columnDefinition = "TEXT")
    private String statistics; // JSON representation of employee presence statistics
}
