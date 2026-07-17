package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.HistorialVacunacion;
import com.petcare.backend.persistence.entity.HistorialVacunacionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, UsuarioMapper.class})
public interface HistorialVacunacionMapper {
    HistorialVacunacion toDomain(HistorialVacunacionEntity entity);

    @Mapping(target = "mascota", ignore = true)
    @Mapping(target = "veterinario", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "actualizadoPor", ignore = true)
    HistorialVacunacionEntity toEntity(HistorialVacunacion domain);
}
