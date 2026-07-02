package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.RecetaVeterinaria;
import com.petcare.backend.persistence.entity.RecetaVeterinariaEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AtencionClinicaMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RecetaVeterinariaMapper {
    RecetaVeterinaria toModel(RecetaVeterinariaEntity entity);
    RecetaVeterinariaEntity toEntity(RecetaVeterinaria model);
}
