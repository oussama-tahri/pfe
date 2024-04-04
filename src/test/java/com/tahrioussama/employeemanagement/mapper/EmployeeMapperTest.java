package com.tahrioussama.employeemanagement.mapper;

import com.tahrioussama.employeemanagement.dtos.EmployeeDTO;
import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.mappers.EmployeeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class EmployeeMapperTest {

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setup() {
        employee = new Employee(1L, "Oussama Tahri", "Casablanca", "Tech", "Engineering", "Senior Engineer");
        employeeDTO = new EmployeeDTO(1L, "Oussama Tahri", "Casablanca", "Tech", "Engineering", "Senior Engineer");
    }

    @Test
    void employeeToDTO() {
        EmployeeDTO mappedDTO = EmployeeMapper.INSTANCE.employeeToDTO(employee);
        assertThat(mappedDTO).isEqualToComparingFieldByField(employeeDTO);
    }

    @Test
    void dtoToEmployee() {
        Employee mappedEmployee = EmployeeMapper.INSTANCE.dtoToEmployee(employeeDTO);
        assertThat(mappedEmployee).isEqualToComparingFieldByField(employee);
    }
}