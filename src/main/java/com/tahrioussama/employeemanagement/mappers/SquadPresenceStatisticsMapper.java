package com.tahrioussama.employeemanagement.mappers;

import com.tahrioussama.employeemanagement.dtos.SquadPresenceStatisticsDTO;
import com.tahrioussama.employeemanagement.entities.SquadPresenceStatistics;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SquadPresenceStatisticsMapper {
    SquadPresenceStatisticsMapper INSTANCE = Mappers.getMapper(SquadPresenceStatisticsMapper.class);

    SquadPresenceStatisticsDTO presenceStatisticsToDTO(SquadPresenceStatistics presenceStatistics);
    SquadPresenceStatistics dtoToPresenceStatistics(SquadPresenceStatisticsDTO presenceStatisticsDTO);
}
