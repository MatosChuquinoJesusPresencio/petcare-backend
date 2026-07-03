package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.HistorialTransferencia;
import com.petcare.backend.persistence.entity.HistorialTransferenciaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, DuenoMapper.class, UsuarioMapper.class})
public interface HistorialTransferenciaMapper {
    HistorialTransferencia toDomain(HistorialTransferenciaEntity entity);

    @Mapping(target = "mascota", ignore = true)
    @Mapping(target = "duenoAnterior", ignore = true)
    @Mapping(target = "duenoNuevo", ignore = true)
    @Mapping(target = "usuarioResponsable", ignore = true)
    HistorialTransferenciaEntity toEntity(HistorialTransferencia domain);
}
