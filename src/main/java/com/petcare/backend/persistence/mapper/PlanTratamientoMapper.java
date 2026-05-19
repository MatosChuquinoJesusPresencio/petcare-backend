package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.PlanTratamiento;
import com.petcare.backend.persistence.entity.PlanTratamientoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, AtencionClinicaMapper.class, UsuarioMapper.class})
public interface PlanTratamientoMapper {
    PlanTratamiento toModel(PlanTratamientoEntity entity);
    PlanTratamientoEntity toEntity(PlanTratamiento model);
}
