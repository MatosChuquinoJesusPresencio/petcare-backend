package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.PlanActividad;
import com.petcare.backend.persistence.entity.PlanActividadEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PlanTratamientoMapper.class, UsuarioMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PlanActividadMapper {
    PlanActividad toModel(PlanActividadEntity entity);
    PlanActividadEntity toEntity(PlanActividad model);
}
