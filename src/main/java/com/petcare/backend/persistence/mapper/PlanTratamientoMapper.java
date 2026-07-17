package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.PlanTratamiento;
import com.petcare.backend.persistence.entity.PlanTratamientoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, UsuarioMapper.class})
public interface PlanTratamientoMapper {
    @Mapping(source = "atencionClinica.id", target = "atencionClinicaId")
    PlanTratamiento toDomain(PlanTratamientoEntity entity);

    @Mapping(target = "mascota", ignore = true)
    @Mapping(target = "veterinario", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "actualizadoPor", ignore = true)
    @Mapping(target = "actividades", ignore = true)
    @Mapping(target = "atencionClinica", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    PlanTratamientoEntity toEntity(PlanTratamiento domain);
}
