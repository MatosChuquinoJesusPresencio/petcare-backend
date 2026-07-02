package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.SalaEspera;
import com.petcare.backend.persistence.entity.SalaEsperaEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CitaMapper.class, MascotaMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SalaEsperaMapper {
    SalaEspera toModel(SalaEsperaEntity entity);
    SalaEsperaEntity toEntity(SalaEspera model);
}
