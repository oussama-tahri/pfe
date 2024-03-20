package com.tahrioussama.employeemanagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @Builder
    public class Presence {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private LocalDate date;
        @Column(columnDefinition="TEXT")
        private String present;

        // Relation avec l'employé
        @ManyToOne
        private Employee employee;
        private String employeeName;
}
