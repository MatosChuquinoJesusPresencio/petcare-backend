package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.persistence.entity.CitaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, UsuarioMapper.class, ServicioMapper.class})
public interface CitaMapper {
    Cita toDomain(CitaEntity entity);

    @Mapping(target = "mascota", ignore = true)
    @Mapping(target = "veterinario", ignore = true)
    @Mapping(target = "servicio", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "actualizadoPor", ignore = true)
    CitaEntity toEntity(Cita domain);
}
