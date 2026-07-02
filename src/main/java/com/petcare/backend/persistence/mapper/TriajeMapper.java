package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Triaje;
import com.petcare.backend.persistence.entity.TriajeEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CitaMapper.class, UsuarioMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TriajeMapper {
    Triaje toModel(TriajeEntity entity);
    TriajeEntity toEntity(Triaje model);
}
