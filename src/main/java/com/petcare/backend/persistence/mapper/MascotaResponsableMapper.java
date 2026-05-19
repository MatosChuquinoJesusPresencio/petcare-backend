package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.MascotaResponsable;
import com.petcare.backend.persistence.entity.MascotaResponsableEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, DuenoMapper.class})
public interface MascotaResponsableMapper {
    MascotaResponsable toModel(MascotaResponsableEntity entity);
    MascotaResponsableEntity toEntity(MascotaResponsable model);
}
