package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.HistorialTransferencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface HistorialTransferenciaRepositoryPort {
    HistorialTransferencia save(HistorialTransferencia transferencia);
    Optional<HistorialTransferencia> findById(Long id);
    Page<HistorialTransferencia> findByMascotaIdOrderByFechaDesc(Long mascotaId, Pageable pageable);
}
