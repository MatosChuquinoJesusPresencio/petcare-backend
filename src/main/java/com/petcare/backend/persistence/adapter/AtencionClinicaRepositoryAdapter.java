package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.AtencionClinica;
import com.petcare.backend.domain.port.AtencionClinicaRepositoryPort;
import com.petcare.backend.persistence.mapper.AtencionClinicaMapper;
import com.petcare.backend.persistence.repository.AtencionClinicaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AtencionClinicaRepositoryAdapter implements AtencionClinicaRepositoryPort {

    private final AtencionClinicaRepository atencionClinicaRepository;
    private final AtencionClinicaMapper atencionClinicaMapper;

    public AtencionClinicaRepositoryAdapter(AtencionClinicaRepository atencionClinicaRepository,
                                             AtencionClinicaMapper atencionClinicaMapper) {
        this.atencionClinicaRepository = atencionClinicaRepository;
        this.atencionClinicaMapper = atencionClinicaMapper;
    }

    @Override
    public AtencionClinica save(AtencionClinica atencionClinica) {
        var entity = atencionClinicaMapper.toEntity(atencionClinica);
        var saved = atencionClinicaRepository.save(entity);
        return atencionClinicaMapper.toModel(saved);
    }

    @Override
    public Optional<AtencionClinica> findById(Long id) {
        return atencionClinicaRepository.findById(id).map(atencionClinicaMapper::toModel);
    }

    @Override
    public Optional<AtencionClinica> findByCitaId(Long citaId) {
        return atencionClinicaRepository.findByCitaId(citaId).map(atencionClinicaMapper::toModel);
    }

    @Override
    public Page<AtencionClinica> findByMascotaIdOrderByCreadoEnDesc(Long mascotaId, Pageable pageable) {
        return atencionClinicaRepository.findByMascotaIdOrderByCreadoEnDesc(mascotaId, pageable)
                .map(atencionClinicaMapper::toModel);
    }

    @Override
    public Page<AtencionClinica> findAll(Pageable pageable) {
        return atencionClinicaRepository.findAll(pageable).map(atencionClinicaMapper::toModel);
    }
}
