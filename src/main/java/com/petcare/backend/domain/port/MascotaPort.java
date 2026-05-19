package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Mascota;
import java.util.Optional;
import java.util.List;

public interface MascotaPort {
    Optional<Mascota> findById(Long id);
    Optional<Mascota> findByMicrochip(String microchip);
    List<Mascota> findAll();
    Mascota save(Mascota mascota);
}
