package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.persistence.entity.ServicioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServicioMapper {
    Servicio toDomain(ServicioEntity entity);

    ServicioEntity toEntity(Servicio domain);
}
