package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.ContactoEmergencia;
import com.petcare.backend.persistence.entity.ContactoEmergenciaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DuenoMapper.class})
public interface ContactoEmergenciaMapper {
    ContactoEmergencia toModel(ContactoEmergenciaEntity entity);
    ContactoEmergenciaEntity toEntity(ContactoEmergencia model);
}
