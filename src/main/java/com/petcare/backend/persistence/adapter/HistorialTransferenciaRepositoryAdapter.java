package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.HistorialTransferencia;
import com.petcare.backend.domain.port.HistorialTransferenciaRepositoryPort;
import com.petcare.backend.persistence.entity.HistorialTransferenciaEntity;
import com.petcare.backend.persistence.mapper.HistorialTransferenciaMapper;
import com.petcare.backend.persistence.repository.DuenoJpaRepository;
import com.petcare.backend.persistence.repository.HistorialTransferenciaJpaRepository;
import com.petcare.backend.persistence.repository.MascotaJpaRepository;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HistorialTransferenciaRepositoryAdapter implements HistorialTransferenciaRepositoryPort {

    private final HistorialTransferenciaJpaRepository historialTransferenciaJpaRepository;
    private final HistorialTransferenciaMapper historialTransferenciaMapper;
    private final MascotaJpaRepository mascotaJpaRepository;
    private final DuenoJpaRepository duenoJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public HistorialTransferenciaRepositoryAdapter(HistorialTransferenciaJpaRepository historialTransferenciaJpaRepository,
                                                   HistorialTransferenciaMapper historialTransferenciaMapper,
                                                   MascotaJpaRepository mascotaJpaRepository,
                                                   DuenoJpaRepository duenoJpaRepository,
                                                   UsuarioJpaRepository usuarioJpaRepository) {
        this.historialTransferenciaJpaRepository = historialTransferenciaJpaRepository;
        this.historialTransferenciaMapper = historialTransferenciaMapper;
        this.mascotaJpaRepository = mascotaJpaRepository;
        this.duenoJpaRepository = duenoJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public HistorialTransferencia save(HistorialTransferencia transferencia) {
        HistorialTransferenciaEntity entity = historialTransferenciaMapper.toEntity(transferencia);
        if (transferencia.getMascota() != null && transferencia.getMascota().getId() != null) {
            entity.setMascota(mascotaJpaRepository.getReferenceById(transferencia.getMascota().getId()));
        }
        if (transferencia.getDuenoAnterior() != null && transferencia.getDuenoAnterior().getId() != null) {
            entity.setDuenoAnterior(duenoJpaRepository.getReferenceById(transferencia.getDuenoAnterior().getId()));
        }
        if (transferencia.getDuenoNuevo() != null && transferencia.getDuenoNuevo().getId() != null) {
            entity.setDuenoNuevo(duenoJpaRepository.getReferenceById(transferencia.getDuenoNuevo().getId()));
        }
        if (transferencia.getUsuarioResponsable() != null && transferencia.getUsuarioResponsable().getId() != null) {
            entity.setUsuarioResponsable(usuarioJpaRepository.getReferenceById(transferencia.getUsuarioResponsable().getId()));
        }
        HistorialTransferenciaEntity saved = historialTransferenciaJpaRepository.save(entity);
        return historialTransferenciaMapper.toDomain(saved);
    }

    @Override
    public Optional<HistorialTransferencia> findById(Long id) {
        return historialTransferenciaJpaRepository.findById(id).map(historialTransferenciaMapper::toDomain);
    }

    @Override
    public Page<HistorialTransferencia> findByMascotaIdOrderByFechaDesc(Long mascotaId, Pageable pageable) {
        return historialTransferenciaJpaRepository.findByMascotaIdOrderByFechaDesc(mascotaId, pageable)
                .map(historialTransferenciaMapper::toDomain);
    }
}
