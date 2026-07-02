package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.BloqueoVeterinario;
import com.petcare.backend.persistence.entity.BloqueoVeterinarioEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BloqueoVeterinarioMapper {
    BloqueoVeterinario toModel(BloqueoVeterinarioEntity entity);
    BloqueoVeterinarioEntity toEntity(BloqueoVeterinario model);
}
