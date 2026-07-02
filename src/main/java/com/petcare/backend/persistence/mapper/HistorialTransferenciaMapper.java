package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.HistorialTransferencia;
import com.petcare.backend.persistence.entity.HistorialTransferenciaEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, DuenoMapper.class, UsuarioMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HistorialTransferenciaMapper {
    HistorialTransferencia toModel(HistorialTransferenciaEntity entity);
    HistorialTransferenciaEntity toEntity(HistorialTransferencia model);
}
