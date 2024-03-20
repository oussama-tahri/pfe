package com.tahrioussama.employeemanagement.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @Builder
public class SquadPresenceStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String squad;

    @Column(columnDefinition = "TEXT")
    private String statistics; // JSON representation of squad presence statistics
}
