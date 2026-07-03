package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.SalaEspera;
import com.petcare.backend.persistence.entity.SalaEsperaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CitaMapper.class)
public interface SalaEsperaMapper {
    SalaEspera toDomain(SalaEsperaEntity entity);

    @Mapping(target = "cita", ignore = true)
    SalaEsperaEntity toEntity(SalaEspera domain);
}
