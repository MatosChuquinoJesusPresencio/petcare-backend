package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.Mascota;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface MascotaRepositoryPort {
    Optional<Mascota> findById(Long id);
    Optional<Mascota> findByMicrochip(String microchip);
    Page<Mascota> findAll(Pageable pageable);
    Page<Mascota> findAll(String nombre, String especie, String raza, String genero, Boolean estado, Long duenoId, Pageable pageable);
    Mascota save(Mascota mascota);
    void deleteById(Long id);
}
