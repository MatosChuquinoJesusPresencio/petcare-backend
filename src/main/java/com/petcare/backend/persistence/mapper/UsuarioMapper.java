package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.persistence.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(target = "contrasena", ignore = true)
    Usuario toDomain(UsuarioEntity entity);

    UsuarioEntity toEntity(Usuario domain);
}
