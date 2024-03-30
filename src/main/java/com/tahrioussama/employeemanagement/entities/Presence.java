package com.tahrioussama.employeemanagement.entities;

import com.tahrioussama.employeemanagement.enums.PresenceStatus;
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
    @Enumerated(EnumType.STRING)
    private PresenceStatus present;

    // Relation avec l'employé
    @ManyToOne
    private Employee employee;
    private String employeeName;
}
