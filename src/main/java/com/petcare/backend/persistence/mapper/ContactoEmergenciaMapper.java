package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.ContactoEmergencia;
import com.petcare.backend.persistence.entity.ContactoEmergenciaEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DuenoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ContactoEmergenciaMapper {
    ContactoEmergencia toModel(ContactoEmergenciaEntity entity);
    ContactoEmergenciaEntity toEntity(ContactoEmergencia model);
}
