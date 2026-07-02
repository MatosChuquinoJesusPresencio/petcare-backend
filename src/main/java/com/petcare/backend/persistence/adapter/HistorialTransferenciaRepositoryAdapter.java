package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.HistorialTransferencia;
import com.petcare.backend.domain.port.HistorialTransferenciaRepositoryPort;
import com.petcare.backend.persistence.mapper.HistorialTransferenciaMapper;
import com.petcare.backend.persistence.repository.HistorialTransferenciaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HistorialTransferenciaRepositoryAdapter implements HistorialTransferenciaRepositoryPort {

    private final HistorialTransferenciaRepository historialTransferenciaRepository;
    private final HistorialTransferenciaMapper historialTransferenciaMapper;

    public HistorialTransferenciaRepositoryAdapter(HistorialTransferenciaRepository historialTransferenciaRepository,
                                                    HistorialTransferenciaMapper historialTransferenciaMapper) {
        this.historialTransferenciaRepository = historialTransferenciaRepository;
        this.historialTransferenciaMapper = historialTransferenciaMapper;
    }

    @Override
    public HistorialTransferencia save(HistorialTransferencia transferencia) {
        var entity = historialTransferenciaMapper.toEntity(transferencia);
        var saved = historialTransferenciaRepository.save(entity);
        return historialTransferenciaMapper.toModel(saved);
    }

    @Override
    public Optional<HistorialTransferencia> findById(Long id) {
        return historialTransferenciaRepository.findById(id).map(historialTransferenciaMapper::toModel);
    }

    @Override
    public Page<HistorialTransferencia> findByMascotaIdOrderByFechaDesc(Long mascotaId, Pageable pageable) {
        return historialTransferenciaRepository.findByMascotaIdOrderByFechaDesc(mascotaId, pageable)
                .map(historialTransferenciaMapper::toModel);
    }
}
