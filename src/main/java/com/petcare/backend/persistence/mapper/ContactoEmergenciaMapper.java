package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.ContactoEmergencia;
import com.petcare.backend.persistence.entity.ContactoEmergenciaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DuenoMapper.class)
public interface ContactoEmergenciaMapper {
    ContactoEmergencia toDomain(ContactoEmergenciaEntity entity);

    @Mapping(target = "dueno", ignore = true)
    ContactoEmergenciaEntity toEntity(ContactoEmergencia domain);
}
