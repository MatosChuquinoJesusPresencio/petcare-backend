package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.AtencionClinica;
import com.petcare.backend.persistence.entity.AtencionClinicaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CitaMapper.class, MascotaMapper.class, UsuarioMapper.class, TriajeMapper.class})
public interface AtencionClinicaMapper {
    AtencionClinica toModel(AtencionClinicaEntity entity);
    AtencionClinicaEntity toEntity(AtencionClinica model);
}
