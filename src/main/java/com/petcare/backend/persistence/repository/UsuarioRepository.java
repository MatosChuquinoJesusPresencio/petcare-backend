package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByUsername(String username);
    Optional<UsuarioEntity> findByEmail(String email);
    List<UsuarioEntity> findByRolAndActivo(String rol, Boolean activo);
}
