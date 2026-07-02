package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Seguimiento;
import com.petcare.backend.persistence.entity.SeguimientoEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AtencionClinicaMapper.class, UsuarioMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SeguimientoMapper {
    Seguimiento toModel(SeguimientoEntity entity);
    SeguimientoEntity toEntity(Seguimiento model);
}
