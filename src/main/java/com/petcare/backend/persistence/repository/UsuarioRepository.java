package com.petcare.backend.persistence.repository;

import com.petcare.backend.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    java.util.Optional<UsuarioEntity> findByUsername(String username);
    java.util.Optional<UsuarioEntity> findByEmail(String email);
}
