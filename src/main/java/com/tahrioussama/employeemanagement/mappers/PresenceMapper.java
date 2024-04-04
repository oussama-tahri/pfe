package com.tahrioussama.employeemanagement.mappers;

import com.tahrioussama.employeemanagement.dtos.PresenceDTO;
import com.tahrioussama.employeemanagement.entities.Presence;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PresenceMapper {
    PresenceMapper INSTANCE = Mappers.getMapper(PresenceMapper.class);

    PresenceDTO presenceToDTO(Presence presence);
    Presence dtoToPresence(PresenceDTO presenceDTO);
}
