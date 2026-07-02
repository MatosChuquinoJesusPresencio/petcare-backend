package com.petcare.backend.persistence.mapper;

import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.persistence.entity.UsuarioEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UsuarioMapper {
    Usuario toModel(UsuarioEntity entity);
    UsuarioEntity toEntity(Usuario model);
}
