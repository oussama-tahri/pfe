package com.tahrioussama.employeemanagement.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor @Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String resourceName;
    private String site;
    private String tribe;
    private String squad;
    private String commentaire;

    public Employee(String resourceName, String site, String tribe, String squad, String commentaire) {
        this.resourceName = resourceName;
        this.site = site;
        this.tribe = tribe;
        this.squad = squad;
        this.commentaire = commentaire;
    }
}
