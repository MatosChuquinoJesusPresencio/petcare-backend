package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.persistence.entity.DuenoEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DuenoMapper {
    Dueno toModel(DuenoEntity entity);
    DuenoEntity toEntity(Dueno model);
}
