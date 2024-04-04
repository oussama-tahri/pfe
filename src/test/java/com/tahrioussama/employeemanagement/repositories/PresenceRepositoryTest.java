package com.tahrioussama.employeemanagement.repositories;

import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.enums.PresenceStatus;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@Transactional
@DataJpaTest
class PresenceRepositoryTest {

    @Autowired
    private PresenceRepository presenceRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        Employee employee = new Employee("Oussama Tahri", "Casablanca", "Dev", "Billing", "Employee in Billing");
        Employee savedEmployee = employeeRepository.save(employee); // Save the employee first
        entityManager.flush(); // Explicitly flush the persistence context to persist the changes
        presenceRepository.save(new Presence(LocalDate.of(2024, 4, 1), PresenceStatus.PRESENT, savedEmployee)); // Use the saved employee when saving Presence
        presenceRepository.save(new Presence(LocalDate.of(2024, 4, 2), PresenceStatus.ABSENT, savedEmployee)); // Use the saved employee when saving Presence
    }



    @Test
    @DisplayName("Test findByEmployee method")
    void testFindByEmployee() {
        // Given
        Employee employee = new Employee("Oussama Tahri", "Casablanca", "Dev", "Billing", "Employee in Billing");

        // When
        List<Presence> presences = presenceRepository.findByEmployee(employee);

        // Then
        assertThat(presences).isNotEmpty();
        assertThat(presences.size()).isEqualTo(2); // Assuming we have added 2 presences for this employee in the setup
    }

    @Test
    @DisplayName("Test countByEmployeeAndDateBetweenAndPresent method")
    void testCountByEmployeeAndDateBetweenAndPresent() {
        // Given
        Employee employee = new Employee("Oussama Tahri", "Casablanca", "Dev", "Billing", "Employee in Billing");
        LocalDate startDate = LocalDate.of(2024, 4, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 2);

        // When
        long count = presenceRepository.countByEmployeeAndDateBetweenAndPresent(employee, startDate, endDate, PresenceStatus.PRESENT);

        // Then
        assertThat(count).isEqualTo(1); // Assuming only 1 presence with PresenceStatus.PRESENT between given dates
    }

    // Add more test methods for other repository methods as needed
}