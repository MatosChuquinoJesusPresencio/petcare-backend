package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);
    List<UsuarioEntity> findByRolAndEstado(String rol, Boolean estado);
    Page<UsuarioEntity> findByRolAndEstado(String rol, Boolean estado, Pageable pageable);
    List<UsuarioEntity> findByRol(String rol);
    Page<UsuarioEntity> findByRol(String rol, Pageable pageable);
}
