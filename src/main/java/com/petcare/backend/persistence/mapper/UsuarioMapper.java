package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.persistence.entity.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    Usuario toDomain(UsuarioEntity entity);

    UsuarioEntity toEntity(Usuario domain);
}
