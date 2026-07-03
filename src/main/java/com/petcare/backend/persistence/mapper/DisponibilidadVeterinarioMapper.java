package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.DisponibilidadVeterinario;
import com.petcare.backend.persistence.entity.DisponibilidadVeterinarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UsuarioMapper.class)
public interface DisponibilidadVeterinarioMapper {
    DisponibilidadVeterinario toDomain(DisponibilidadVeterinarioEntity entity);

    @Mapping(target = "veterinario", ignore = true)
    DisponibilidadVeterinarioEntity toEntity(DisponibilidadVeterinario domain);
}
