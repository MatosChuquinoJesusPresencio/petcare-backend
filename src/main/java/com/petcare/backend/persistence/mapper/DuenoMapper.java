package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.persistence.entity.DuenoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface DuenoMapper {
    Dueno toModel(DuenoEntity entity);
    DuenoEntity toEntity(Dueno model);
}
