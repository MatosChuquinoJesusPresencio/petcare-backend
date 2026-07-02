package com.petcare.backend.domain.port;

import com.petcare.backend.domain.model.AtencionClinica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AtencionClinicaRepositoryPort {
    AtencionClinica save(AtencionClinica atencionClinica);
    Optional<AtencionClinica> findById(Long id);
    Optional<AtencionClinica> findByCitaId(Long citaId);
    Page<AtencionClinica> findByMascotaIdOrderByCreadoEnDesc(Long mascotaId, Pageable pageable);
    Page<AtencionClinica> findAll(Pageable pageable);
}
