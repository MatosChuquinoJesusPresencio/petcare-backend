package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.persistence.entity.MascotaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MascotaMapper {
    Mascota toModel(MascotaEntity entity);
    MascotaEntity toEntity(Mascota model);
}
