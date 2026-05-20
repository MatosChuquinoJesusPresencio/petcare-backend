package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Mascota;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface MascotaRepositoryPort {
    Optional<Mascota> findById(Long id);
    Optional<Mascota> findByMicrochip(String microchip);
    Page<Mascota> findAll(Pageable pageable);
    Mascota save(Mascota mascota);
}
