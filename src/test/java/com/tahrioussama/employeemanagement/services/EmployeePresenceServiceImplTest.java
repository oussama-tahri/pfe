package com.tahrioussama.employeemanagement.services;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.EmployeePresenceStatistics;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.repositories.EmployeePresenceStatisticsRepository;
import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import com.tahrioussama.employeemanagement.services.EmployeePresenceServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmployeePresenceServiceImplTest {

    @Mock
    private PresenceRepository presenceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeePresenceStatisticsRepository employeePresenceStatisticsRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EmployeePresenceServiceImpl presenceService;

    @Test
    public void testUpdatePresenceValueForDate() throws JsonProcessingException {
        // Mock data
        Employee employee = new Employee(1L, "John Doe", "Site", "Tribe", "Squad", "Commentaire");
        LocalDate providedDate = LocalDate.of(2024, Month.MARCH, 15);
        Presence presence1 = new Presence(1L, providedDate, true, employee, "John Doe");
        Presence presence2 = new Presence(2L, providedDate, true, employee, "John Doe");

        // Mock repository behavior
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(presenceRepository.findByEmployeeAndDate(employee, providedDate)).thenReturn(Arrays.asList(presence1, presence2));

        // Execute the method
        presenceService.updatePresenceValueForDate(1L, providedDate, false);

        // Verify that presence values are updated
        verify(presenceRepository, times(1)).save(presence1);
        verify(presenceRepository, times(1)).save(presence2);

        // Verify that presence statistics are updated
        verify(employeePresenceStatisticsRepository, times(1)).save(any(EmployeePresenceStatistics.class));
    }
}