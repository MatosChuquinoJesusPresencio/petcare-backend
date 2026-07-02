package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Consentimiento;
import com.petcare.backend.persistence.entity.ConsentimientoEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, DuenoMapper.class, UsuarioMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ConsentimientoMapper {
    Consentimiento toModel(ConsentimientoEntity entity);
    ConsentimientoEntity toEntity(Consentimiento model);
}
