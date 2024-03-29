package com.tahrioussama.employeemanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.entities.SquadPresenceStatistics;
import com.tahrioussama.employeemanagement.repositories.EmployeePresenceStatisticsRepository;
import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import com.tahrioussama.employeemanagement.repositories.SquadPresenceStatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceImplTest {

    @Mock
    private PresenceRepository presenceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeePresenceStatisticsRepository employeePresenceStatisticsRepository;

    @Mock
    private SquadPresenceStatisticsRepository squadPresenceStatisticsRepository;
    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateAndSaveEmployeePresenceStatistics() {
        // Mock employee data
        Employee employee1 = new Employee("John", "Site1", "Tribe1", "Squad1", "Comment1");
        Employee employee2 = new Employee("Alice", "Site2", "Tribe2", "Squad2", "Comment2");
        lenient().when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));

        // Mock presence data
        Presence presence1 = new Presence(1L, LocalDate.of(2024, Month.MARCH, 1), true, employee1, "John");
        Presence presence2 = new Presence(2L, LocalDate.of(2024, Month.MARCH, 2), false, employee1, "John");
        Presence presence3 = new Presence(3L, LocalDate.of(2024, Month.MARCH, 3), true, employee1, "John");
        Presence presence4 = new Presence(4L, LocalDate.of(2024, Month.MARCH, 4), true, employee1, "John");
        Presence presence5 = new Presence(5L, LocalDate.of(2024, Month.MARCH, 5), true, employee1, "John");
        lenient().when(presenceRepository.findByEmployee(employee1)).thenReturn(List.of(presence1, presence2, presence3, presence4, presence5));

        // Call the method under test
        statisticsService.calculateAndSaveEmployeePresenceStatistics();

        // Verify that the save method is called for each employee
        verify(employeePresenceStatisticsRepository, times(2)).save(any());
    }

    @Test
    public void testCalculateAndSaveSquadPresenceStatistics() {
        // Create a dummy Employee
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setSquad("Test Squad");

        // Create a dummy Presence associated with the Employee
        Presence presence = new Presence();
        presence.setId(1L);
        presence.setEmployee(employee);
        // Mock the presenceRepository to return a list with this presence
        when(presenceRepository.findAll()).thenReturn(Collections.singletonList(presence));

        // Invoke the method under test
        statisticsService.calculateAndSaveSquadPresenceStatistics();

        // Verify that squadPresenceStatisticsRepository.save is called with a non-null argument
        verify(squadPresenceStatisticsRepository, times(1)).save(any(SquadPresenceStatistics.class));
    }

    @Test
    void testCalculateSquadPresencePercentagePerWeek() {
        // Mock presence data
        Employee employee = new Employee("John", "Site1", "Tribe1", "Squad1", "Comment1");
        when(employeeRepository.countBySquad(anyString())).thenReturn(1L);
        when(presenceRepository.countDistinctEmployeesBySquadAndWeekStartDate(anyString(), any(LocalDate.class), any(LocalDate.class))).thenReturn(1L);

        // Call the method under test
        double percentage = statisticsService.calculateSquadPresencePercentagePerWeek("Squad1", LocalDate.of(2024, Month.MARCH, 1));

        // Verify the result
        assertEquals(100.0, percentage);
    }

    @Test
    void testCalculateSquadPresencePercentagePerMonth() {
        // Mock presence data
        Employee employee = new Employee("John", "Site1", "Tribe1", "Squad1", "Comment1");
        when(employeeRepository.countBySquad(anyString())).thenReturn(1L);
        when(presenceRepository.countDistinctEmployeesBySquadAndMonthStartDate(anyString(), any(LocalDate.class))).thenReturn(1L);

        // Call the method under test
        double percentage = statisticsService.calculateSquadPresencePercentagePerMonth("Squad1", LocalDate.of(2024, Month.MARCH, 1));

        // Verify the result
        assertEquals(100.0, percentage);
    }

    @Test
    public void testCalculateEmployeePresenceStatus() {
        // Mock employee
        String employeeName = "John Doe";
        String squad = "Squad1";
        Employee testEmployee = new Employee();
        testEmployee.setResourceName(employeeName);
        testEmployee.setSquad(squad);

        // Mock employee repository
        when(employeeRepository.findByResourceName(employeeName)).thenReturn(Optional.of(testEmployee));

        // Mock squad presence statistics
        String squadName = "Squad1";
        SquadPresenceStatistics squadStats = new SquadPresenceStatistics();
        squadStats.setSquad(squadName);
        squadStats.setStatistics("{\"weeklyPresence\":{\"Week 1\":0,\"Week 2\":0,\"Week 3\":0, \"Week 4\":0},\"monthlyPresence\":{\"Month\":[\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\"]}}");
        when(squadPresenceStatisticsRepository.findBySquad(anyString())).thenReturn(Optional.of(squadStats));

        // Invoke the method under test
        String presenceStatus = statisticsService.calculateEmployeePresenceStatus(employeeName);

        // Expected presence status
        String expectedStatus = "Employee John Doe presence status:\n" +
                "Weekly presence:\n" +
                "Absent in Week 1\n" +
                "Absent in Week 2\n" +
                "Absent in Week 3\n" +
                "Absent in Week 4\n" +
                "Monthly presence:\n" +
                "Absent on MONDAY\n" +
                "Absent on TUESDAY\n" +
                "Absent on WEDNESDAY\n" +
                "Absent on THURSDAY\n" +
                "Absent on FRIDAY\n";

        // Verify that the correct status is returned
        assertEquals(expectedStatus, presenceStatus, "Presence status does not match expected status");
    }
}
