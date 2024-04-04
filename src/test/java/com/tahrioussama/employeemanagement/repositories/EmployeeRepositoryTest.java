package com.tahrioussama.employeemanagement.repositories;

import com.tahrioussama.employeemanagement.entities.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Test count employees by squad")
    void testCountEmployeesBySquad() {
        // Given
        Employee employee1 = new Employee();
        employee1.setResourceName("Oussama Tahri");
        employee1.setSquad("Squad A");

        Employee employee2 = new Employee();
        employee2.setResourceName("Erraji Abdelmonaim");
        employee2.setSquad("Squad A");

        Employee employee3 = new Employee();
        employee3.setResourceName("ElAdnani Inssaf");
        employee3.setSquad("Squad B");

        // Save employees to the database
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        // When
        long count = employeeRepository.countBySquad("Squad A");

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Test find employee by resource name")
    void testFindEmployeeByResourceName() {
        // Given
        Employee employee = new Employee();
        employee.setResourceName("Oussama Tahri");

        // Save employee to the database
        employeeRepository.save(employee);

        // When
        Optional<Employee> foundEmployeeOptional = employeeRepository.findByResourceName("Oussama Tahri");

        // Then
        assertThat(foundEmployeeOptional).isPresent();
        Employee foundEmployee = foundEmployeeOptional.get();
        assertThat(foundEmployee.getResourceName()).isEqualTo("Oussama Tahri");
    }
}