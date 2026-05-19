package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.AuditoriaClinica;
import com.petcare.backend.persistence.entity.AuditoriaClinicaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class})
public interface AuditoriaClinicaMapper {
    AuditoriaClinica toModel(AuditoriaClinicaEntity entity);
    AuditoriaClinicaEntity toEntity(AuditoriaClinica model);
}
