package com.tahrioussama.employeemanagement.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SquadPresenceStatisticsDTO {
    private Long id;
    private String squad;
    private String statistics;
}
