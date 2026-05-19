package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.RefreshToken;
import com.petcare.backend.persistence.entity.RefreshTokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    RefreshToken toModel(RefreshTokenEntity entity);
    RefreshTokenEntity toEntity(RefreshToken model);
}
