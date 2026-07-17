package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Receta;
import com.petcare.backend.persistence.entity.RecetaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MascotaMapper.class, UsuarioMapper.class})
public interface RecetaMapper {
    @Mapping(source = "atencionClinica.id", target = "atencionClinicaId")
    Receta toDomain(RecetaEntity entity);

    @Mapping(target = "mascota", ignore = true)
    @Mapping(target = "veterinario", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "actualizadoPor", ignore = true)
    @Mapping(target = "detalles", ignore = true)
    @Mapping(target = "atencionClinica", ignore = true)
    @Mapping(target = "actualizadoEn", ignore = true)
    RecetaEntity toEntity(Receta domain);
}
