package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Servicio;
import java.util.Optional;
import java.util.List;

public interface ServicioRepositoryPort {
    Optional<Servicio> findById(Long id);
    List<Servicio> findAll();
    List<Servicio> findByActivo(Boolean activo);
    Servicio save(Servicio servicio);
}
