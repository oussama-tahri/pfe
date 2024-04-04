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
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Employee employee;
    private String employeeName;

    public Presence(LocalDate date, PresenceStatus present, Employee employee) {
        this.date = date;
        this.present = present;
        this.employee = employee;
    }

    public Presence(Long id, LocalDate date, PresenceStatus present, Employee employee) {
        this.id = id;
        this.date = date;
        this.present = present;
        this.employee = employee;
    }
}
