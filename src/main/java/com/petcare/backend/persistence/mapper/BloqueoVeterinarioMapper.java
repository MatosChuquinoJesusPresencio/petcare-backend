package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.BloqueoVeterinario;
import com.petcare.backend.persistence.entity.BloqueoVeterinarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UsuarioMapper.class)
public interface BloqueoVeterinarioMapper {
    BloqueoVeterinario toDomain(BloqueoVeterinarioEntity entity);

    @Mapping(target = "veterinario", ignore = true)
    BloqueoVeterinarioEntity toEntity(BloqueoVeterinario domain);
}
