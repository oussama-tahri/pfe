package com.tahrioussama.employeemanagement.mappers;

import com.tahrioussama.employeemanagement.dtos.EmployeePresenceStatisticsDTO;
import com.tahrioussama.employeemanagement.entities.EmployeePresenceStatistics;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeePresenceStatisticsMapper {
    EmployeePresenceStatisticsMapper INSTANCE = Mappers.getMapper(EmployeePresenceStatisticsMapper.class);
    EmployeePresenceStatisticsDTO presenceStatisticsToDTO(EmployeePresenceStatistics presenceStatistics);
    EmployeePresenceStatistics dtoToPresenceStatistics(EmployeePresenceStatisticsDTO presenceStatisticsDTO);
}
