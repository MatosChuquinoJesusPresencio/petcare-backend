package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.RecetaDetalle;
import com.petcare.backend.persistence.entity.RecetaDetalleEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RecetaVeterinariaMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RecetaDetalleMapper {
    RecetaDetalle toModel(RecetaDetalleEntity entity);
    RecetaDetalleEntity toEntity(RecetaDetalle model);
}
