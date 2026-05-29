package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Servicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ServicioRepositoryPort {
    Optional<Servicio> findById(Long id);
    Page<Servicio> findAll(Pageable pageable);
    Page<Servicio> findByActivo(Boolean activo, Pageable pageable);
    Page<Servicio> findByNombre(String nombre, Pageable pageable);
    Page<Servicio> findByActivoAndNombre(Boolean activo, String nombre, Pageable pageable);
    Servicio save(Servicio servicio);
    void deleteById(Long id);
}
