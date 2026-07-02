package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.VacunacionDesparasitacion;
import com.petcare.backend.persistence.entity.VacunacionDesparasitacionEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, UsuarioMapper.class, AtencionClinicaMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VacunacionDesparasitacionMapper {
    VacunacionDesparasitacion toModel(VacunacionDesparasitacionEntity entity);
    VacunacionDesparasitacionEntity toEntity(VacunacionDesparasitacion model);
}
