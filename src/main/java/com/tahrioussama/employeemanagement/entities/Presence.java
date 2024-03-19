package com.tahrioussama.employeemanagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @Builder
    public class Presence {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "employee_id", nullable = false)
        private Employee employee;

        private LocalDate date;
        private Boolean presentOnSite;
}
