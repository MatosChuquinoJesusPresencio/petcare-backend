package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.persistence.entity.ServicioEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ServicioMapper {
    Servicio toModel(ServicioEntity entity);
    ServicioEntity toEntity(Servicio model);
}
