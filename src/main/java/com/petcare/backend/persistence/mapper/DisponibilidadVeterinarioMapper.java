package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.DisponibilidadVeterinario;
import com.petcare.backend.persistence.entity.DisponibilidadVeterinarioEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DisponibilidadVeterinarioMapper {
    DisponibilidadVeterinario toModel(DisponibilidadVeterinarioEntity entity);
    DisponibilidadVeterinarioEntity toEntity(DisponibilidadVeterinario model);
}
