package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Seguimiento;
import com.petcare.backend.persistence.entity.SeguimientoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AtencionClinicaMapper.class, UsuarioMapper.class})
public interface SeguimientoMapper {
    Seguimiento toModel(SeguimientoEntity entity);
    SeguimientoEntity toEntity(Seguimiento model);
}
