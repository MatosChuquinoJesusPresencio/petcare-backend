package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Triaje;
import com.petcare.backend.persistence.entity.TriajeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CitaMapper.class, UsuarioMapper.class})
public interface TriajeMapper {
    Triaje toDomain(TriajeEntity entity);

    @Mapping(target = "cita", ignore = true)
    @Mapping(target = "asistente", ignore = true)
    TriajeEntity toEntity(Triaje domain);
}
