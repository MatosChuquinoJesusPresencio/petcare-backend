package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import java.util.List;

public interface UsuarioRepositoryPort {
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    Page<Usuario> findAll(Pageable pageable);
    Usuario save(Usuario usuario);
    List<Usuario> findByRolAndEstado(String rol, Boolean estado);
    Page<Usuario> findByRolAndEstado(String rol, Boolean estado, Pageable pageable);
    List<Usuario> findByRol(String rol);
    Page<Usuario> findByRol(String rol, Pageable pageable);
}
