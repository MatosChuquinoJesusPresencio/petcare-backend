package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Receta;
import java.util.List;
import java.util.Optional;

public interface RecetaRepositoryPort {
    Optional<Receta> findById(Long id);
    List<Receta> findByAtencionClinicaId(Long atencionClinicaId);
    List<Receta> findByMascotaId(Long mascotaId);
    Receta save(Receta receta);
    void deleteById(Long id);
}
