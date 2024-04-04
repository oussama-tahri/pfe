package com.tahrioussama.employeemanagement.mappers;

import com.tahrioussama.employeemanagement.dtos.EmployeeDTO;
import com.tahrioussama.employeemanagement.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDTO employeeToDTO(Employee employee);
    Employee dtoToEmployee(EmployeeDTO employeeDTO);
}