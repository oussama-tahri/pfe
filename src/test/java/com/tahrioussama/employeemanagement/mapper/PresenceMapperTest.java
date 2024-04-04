package com.tahrioussama.employeemanagement.mapper;

import com.tahrioussama.employeemanagement.dtos.EmployeeDTO;
import com.tahrioussama.employeemanagement.dtos.PresenceDTO;
import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.enums.PresenceStatus;
import com.tahrioussama.employeemanagement.mappers.PresenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class PresenceMapperTest {

    private Presence presence;
    private PresenceDTO presenceDTO;

    @BeforeEach
    void setup() {
        Employee employee = new Employee(1L, "Oussama Tahri", "Casablanca", "Tech", "Engineering", "Senior Engineer");
        presence = new Presence(1L, LocalDate.now(), PresenceStatus.PRESENT, employee);
        presenceDTO = new PresenceDTO(1L, LocalDate.now(), PresenceStatus.PRESENT, new EmployeeDTO(1L, "Oussama Tahri", "Casablanca", "Tech", "Engineering", "Senior Engineer"));
    }

    @Test
    void presenceToDTO() {
        PresenceDTO mappedDTO = PresenceMapper.INSTANCE.presenceToDTO(presence);
        assertThat(mappedDTO).isEqualToComparingFieldByField(presenceDTO);
    }

    @Test
    void dtoToPresence() {
        Presence mappedPresence = PresenceMapper.INSTANCE.dtoToPresence(presenceDTO);
        assertThat(mappedPresence).isEqualToComparingFieldByField(presence);
    }
}