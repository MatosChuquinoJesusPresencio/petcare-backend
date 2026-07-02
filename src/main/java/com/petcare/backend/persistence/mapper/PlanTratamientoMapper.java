package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.PlanTratamiento;
import com.petcare.backend.persistence.entity.PlanTratamientoEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, AtencionClinicaMapper.class, UsuarioMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PlanTratamientoMapper {
    PlanTratamiento toModel(PlanTratamientoEntity entity);
    PlanTratamientoEntity toEntity(PlanTratamiento model);
}
