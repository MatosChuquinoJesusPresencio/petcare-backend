package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.persistence.entity.CitaEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, UsuarioMapper.class, ServicioMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CitaMapper {
    Cita toModel(CitaEntity entity);
    CitaEntity toEntity(Cita model);
}
