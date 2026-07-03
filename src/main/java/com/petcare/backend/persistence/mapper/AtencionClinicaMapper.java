package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.AtencionClinica;
import com.petcare.backend.persistence.entity.AtencionClinicaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CitaMapper.class, MascotaMapper.class, UsuarioMapper.class})
public interface AtencionClinicaMapper {
    AtencionClinica toDomain(AtencionClinicaEntity entity);

    @Mapping(target = "cita", ignore = true)
    @Mapping(target = "mascota", ignore = true)
    @Mapping(target = "veterinario", ignore = true)
    @Mapping(target = "triaje", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "actualizadoPor", ignore = true)
    AtencionClinicaEntity toEntity(AtencionClinica domain);
}
