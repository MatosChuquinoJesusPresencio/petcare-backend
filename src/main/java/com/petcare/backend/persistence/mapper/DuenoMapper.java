package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.persistence.entity.DuenoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UsuarioMapper.class)
public interface DuenoMapper {
    Dueno toDomain(DuenoEntity entity);

    @Mapping(target = "usuario", ignore = true)
    DuenoEntity toEntity(Dueno domain);
}
