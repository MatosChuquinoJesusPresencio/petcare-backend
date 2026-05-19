package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Usuario;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepositoryPort {
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findAll();
    Usuario save(Usuario usuario);
}
