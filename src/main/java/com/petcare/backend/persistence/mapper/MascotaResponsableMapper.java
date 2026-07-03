package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.MascotaResponsable;
import com.petcare.backend.persistence.entity.MascotaResponsableEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, DuenoMapper.class})
public interface MascotaResponsableMapper {
    MascotaResponsable toDomain(MascotaResponsableEntity entity);

    @Mapping(target = "mascota", ignore = true)
    @Mapping(target = "dueno", ignore = true)
    MascotaResponsableEntity toEntity(MascotaResponsable domain);
}
