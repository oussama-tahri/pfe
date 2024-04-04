package com.tahrioussama.employeemanagement.services;

import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.enums.PresenceStatus;
import com.tahrioussama.employeemanagement.exceptions.EmployeeNotFoundException;
import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import com.tahrioussama.employeemanagement.repositories.EmployeePresenceStatisticsRepository;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmployeePresenceServiceImplTest {

    @Mock
    private PresenceRepository presenceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeePresenceStatisticsRepository employeePresenceStatisticsRepository;

    @InjectMocks
    private EmployeePresenceServiceImpl employeePresenceService;

    private Employee mockEmployee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockEmployee = new Employee();
        mockEmployee.setId(1L);
        mockEmployee.setResourceName("Oussama Tahri");
    }

    @Test
    @DisplayName("Test update presence value for date")
    void testUpdatePresenceValueForDate() {
        LocalDate providedDate = LocalDate.of(2024, 3, 1);

        // Mock repository method
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));

        // Invoke the method and assert that it throws EmployeeNotFoundException
        assertThrows(EmployeeNotFoundException.class,
                () -> employeePresenceService.updatePresenceValueForDate(2L, providedDate, PresenceStatus.PRESENT));

        // Ensure that findById is called with the correct argument
        verify(employeeRepository, times(1)).findById(2L);
    }
}