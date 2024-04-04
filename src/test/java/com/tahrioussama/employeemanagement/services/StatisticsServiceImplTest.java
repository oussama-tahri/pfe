package com.tahrioussama.employeemanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tahrioussama.employeemanagement.config.JsonUtils;
import com.tahrioussama.employeemanagement.dtos.EmployeeDTO;
import com.tahrioussama.employeemanagement.dtos.EmployeePresenceStatisticsDTO;
import com.tahrioussama.employeemanagement.dtos.PresenceDTO;
import com.tahrioussama.employeemanagement.dtos.SquadPresenceStatisticsDTO;
import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.EmployeePresenceStatistics;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.entities.SquadPresenceStatistics;
import com.tahrioussama.employeemanagement.enums.PresenceStatus;
import com.tahrioussama.employeemanagement.exceptions.EmployeeNotFoundException;
import com.tahrioussama.employeemanagement.exceptions.NoSquadAssignedException;
import com.tahrioussama.employeemanagement.exceptions.PresenceStatisticsNotFoundException;
import com.tahrioussama.employeemanagement.mappers.EmployeeMapper;
import com.tahrioussama.employeemanagement.mappers.EmployeePresenceStatisticsMapper;
import com.tahrioussama.employeemanagement.mappers.PresenceMapper;
import com.tahrioussama.employeemanagement.mappers.SquadPresenceStatisticsMapper;
import com.tahrioussama.employeemanagement.repositories.EmployeePresenceStatisticsRepository;
import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import com.tahrioussama.employeemanagement.repositories.SquadPresenceStatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static com.tahrioussama.employeemanagement.config.JsonUtils.jsonStringToMap;
import static com.tahrioussama.employeemanagement.config.JsonUtils.objectMapper;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {

    @Mock
    private PresenceRepository presenceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeePresenceStatisticsRepository employeePresenceStatisticsRepository;

    @Mock
    private SquadPresenceStatisticsRepository squadPresenceStatisticsRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private PresenceMapper presenceMapper;

    @Mock
    private EmployeePresenceStatisticsMapper employeePresenceStatisticsMapper;

    @Mock
    private SquadPresenceStatisticsMapper squadPresenceStatisticsMapper;
    @Mock
    JsonUtils jsonUtils;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    private Employee mockEmployee;
    private Presence mockPresence;

    @BeforeEach
    void setUp() {
        // Initialize mock objects
        mockEmployee = new Employee();
        mockEmployee.setId(1L);
        mockEmployee.setResourceName("Oussama Tahri");

        mockPresence = new Presence();
        mockPresence.setId(1L);
        mockPresence.setEmployee(mockEmployee);
        mockPresence.setDate(LocalDate.now());
        mockPresence.setPresent(PresenceStatus.PRESENT);
    }

    @Test
    @DisplayName("Test get all employees")
    void testGetAllEmployees() {
        // Mock data
        List<Employee> employees = new ArrayList<>();
        employees.add(mockEmployee);

        // Mock repository method
        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.employeeToDTO(any(Employee.class))).thenReturn(new EmployeeDTO());

        // Call the service method
        List<EmployeeDTO> result = statisticsService.getAllEmployees();

        // Verify
        assertEquals(employees.size(), result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test get presence")
    void testGetPresence() {
        // Mock data
        List<Presence> presences = new ArrayList<>();
        presences.add(mockPresence);

        // Mock repository method
        when(presenceRepository.findAll()).thenReturn(presences);
        when(presenceMapper.presenceToDTO(any(Presence.class))).thenReturn(new PresenceDTO());

        // Call the service method
        List<PresenceDTO> result = statisticsService.getPresence();

        // Verify
        assertEquals(presences.size(), result.size());
        verify(presenceRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test calculate and save employee presence statistics")
    void testCalculateAndSaveEmployeePresenceStatistics() {
        // Mock employee repository to return a sample employee
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(mockEmployee));

        // Mock presence repository to return sample presences
        List<Presence> presences = Arrays.asList(
                mockPresence,
                new Presence(LocalDate.of(2024, Month.MARCH, 3), PresenceStatus.PRESENT, mockEmployee),
                new Presence(LocalDate.of(2024, Month.MARCH, 7), PresenceStatus.PRESENT, mockEmployee)
        );
        when(presenceRepository.countByEmployeeAndDateBetweenAndPresent(any(), any(), any(), any())).thenReturn(3L);
        when(presenceRepository.findByEmployeeAndPresent(any(), any())).thenReturn(presences);

        // Invoke the method
        assertDoesNotThrow(() -> statisticsService.calculateAndSaveEmployeePresenceStatistics());

        // Verify that employeePresenceStatisticsRepository.save is called once
        verify(employeePresenceStatisticsRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test calculate and save squad presence statistics")
    void testCalculateAndSaveSquadPresenceStatistics() {
        // Mock data
        List<Presence> presences = Collections.singletonList(mockPresence);

        Map<String, Object> statsMap = new HashMap<>();
        statsMap.put("weeklyPresence", Collections.singletonMap("Week 1", 5));
        statsMap.put("monthlyPresence", Collections.singletonMap("Month", Arrays.asList("MONDAY", "TUESDAY")));

        // Mock repository method
        when(presenceRepository.findAll()).thenReturn(presences);
        when(squadPresenceStatisticsMapper.dtoToPresenceStatistics(any(SquadPresenceStatisticsDTO.class))).thenReturn(new SquadPresenceStatistics());

        // Call the service method
        statisticsService.calculateAndSaveSquadPresenceStatistics();

        // Verify
        verify(presenceRepository, times(1)).findAll();
        verify(squadPresenceStatisticsRepository, times(1)).save(any(SquadPresenceStatistics.class));
    }

    @Test
    @DisplayName("Test calculate squad presence percentage per week")
    void testCalculateSquadPresencePercentagePerWeek() {
        // Mock data
        String squadName = "Squad A";
        LocalDate weekStartDate = LocalDate.now();

        // Mock repository method
        when(employeeRepository.countBySquad(squadName)).thenReturn(10L);
        when(presenceRepository.countDistinctEmployeesBySquadAndWeekStartDate(squadName, weekStartDate, weekStartDate.plusDays(7))).thenReturn(5L);

        // Call the service method
        double result = statisticsService.calculateSquadPresencePercentagePerWeek(squadName, weekStartDate);

        // Verify
        assertEquals(50, result);
    }

    @Test
    @DisplayName("Test calculate squad presence percentage per month")
    void testCalculateSquadPresencePercentagePerMonth() {
        // Mock data
        String squadName = "Squad A";
        LocalDate monthStartDate = LocalDate.now();

        // Mock repository method
        when(employeeRepository.countBySquad(squadName)).thenReturn(10L);
        when(presenceRepository.countDistinctEmployeesBySquadAndMonthStartDate(squadName, monthStartDate)).thenReturn(5L);

        // Call the service method
        double result = statisticsService.calculateSquadPresencePercentagePerMonth(squadName, monthStartDate);

        // Verify
        assertEquals(50, result);
    }

    @Test
    @DisplayName("Test calculate employee presence status")
    void testCalculateEmployeePresenceStatus() {
        // Mock employee repository to return a sample employee without a squad assigned
        Employee employeeWithoutSquad = new Employee();
        employeeWithoutSquad.setResourceName("Oussama Tahri");
        when(employeeRepository.findByResourceName(anyString())).thenReturn(Optional.of(employeeWithoutSquad));

        // Invoke the method and assert that it throws NoSquadAssignedException
        assertThrows(NoSquadAssignedException.class, () -> statisticsService.calculateEmployeePresenceStatus("Oussama Tahri"));

        // Verify that findByEmployeeAndPresent is not called since the employee has no squad assigned
        verifyNoInteractions(presenceRepository);
    }


    @Test
    @DisplayName("Test calculate employee presence status when employee not found")
    void testCalculateEmployeePresenceStatusEmployeeNotFound() {
        // Mock data
        String employeeName = "Oussama Tahri";

        // Mock repository method
        when(employeeRepository.findByResourceName(employeeName)).thenReturn(Optional.empty());

        // Verify that EmployeeNotFoundException is thrown
        assertThrows(EmployeeNotFoundException.class, () -> statisticsService.calculateEmployeePresenceStatus(employeeName));

        // Ensure that findByResourceName is called with the correct argument
        verify(employeeRepository, times(1)).findByResourceName(employeeName);
    }

    @Test
    @DisplayName("Test calculate employee presence status when squad not assigned")
    void testCalculateEmployeePresenceStatusSquadNotAssigned() {
        // Mock data
        String employeeName = "Oussama Tahri";

        // Mock repository method
        when(employeeRepository.findByResourceName(employeeName)).thenReturn(Optional.of(mockEmployee));

        // Verify that NoSquadAssignedException is thrown
        assertThrows(NoSquadAssignedException.class, () -> statisticsService.calculateEmployeePresenceStatus(employeeName));
    }

    @Test
    void testCalculateEmployeePresenceStatusSquadStatisticsNotFound() {
        // Mock employee repository to return a sample employee
        when(employeeRepository.findByResourceName(anyString())).thenReturn(Optional.of(mockEmployee));

        // Invoke the method and assert that it throws NoSquadAssignedException
        assertThrows(NoSquadAssignedException.class, () -> statisticsService.calculateEmployeePresenceStatus("Oussama Tahri"));
    }
}
