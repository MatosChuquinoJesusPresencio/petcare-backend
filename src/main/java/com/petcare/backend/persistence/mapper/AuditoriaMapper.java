package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Auditoria;
import com.petcare.backend.persistence.entity.AuditoriaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface AuditoriaMapper {
    Auditoria toDomain(AuditoriaEntity entity);

    @Mapping(target = "usuario", ignore = true)
    AuditoriaEntity toEntity(Auditoria domain);
}
