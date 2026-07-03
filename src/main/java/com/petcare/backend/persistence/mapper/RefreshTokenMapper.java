package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.RefreshToken;
import com.petcare.backend.persistence.entity.RefreshTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UsuarioMapper.class)
public interface RefreshTokenMapper {
    RefreshToken toDomain(RefreshTokenEntity entity);

    @Mapping(target = "usuario", ignore = true)
    RefreshTokenEntity toEntity(RefreshToken domain);
}
