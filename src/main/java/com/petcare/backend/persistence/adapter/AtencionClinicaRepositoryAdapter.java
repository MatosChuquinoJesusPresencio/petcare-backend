package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.AtencionClinica;
import com.petcare.backend.domain.port.AtencionClinicaRepositoryPort;
import com.petcare.backend.persistence.entity.AtencionClinicaEntity;
import com.petcare.backend.persistence.mapper.AtencionClinicaMapper;
import com.petcare.backend.persistence.repository.AtencionClinicaJpaRepository;
import com.petcare.backend.persistence.repository.CitaJpaRepository;
import com.petcare.backend.persistence.repository.MascotaJpaRepository;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AtencionClinicaRepositoryAdapter implements AtencionClinicaRepositoryPort {

    private final AtencionClinicaJpaRepository atencionClinicaJpaRepository;
    private final AtencionClinicaMapper atencionClinicaMapper;
    private final CitaJpaRepository citaJpaRepository;
    private final MascotaJpaRepository mascotaJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public AtencionClinicaRepositoryAdapter(AtencionClinicaJpaRepository atencionClinicaJpaRepository,
                                            AtencionClinicaMapper atencionClinicaMapper,
                                            CitaJpaRepository citaJpaRepository,
                                            MascotaJpaRepository mascotaJpaRepository,
                                            UsuarioJpaRepository usuarioJpaRepository) {
        this.atencionClinicaJpaRepository = atencionClinicaJpaRepository;
        this.atencionClinicaMapper = atencionClinicaMapper;
        this.citaJpaRepository = citaJpaRepository;
        this.mascotaJpaRepository = mascotaJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public AtencionClinica save(AtencionClinica atencionClinica) {
        AtencionClinicaEntity entity = atencionClinicaMapper.toEntity(atencionClinica);
        if (atencionClinica.getCita() != null && atencionClinica.getCita().getId() != null) {
            entity.setCita(citaJpaRepository.getReferenceById(atencionClinica.getCita().getId()));
        }
        if (atencionClinica.getMascota() != null && atencionClinica.getMascota().getId() != null) {
            entity.setMascota(mascotaJpaRepository.getReferenceById(atencionClinica.getMascota().getId()));
        }
        if (atencionClinica.getVeterinario() != null && atencionClinica.getVeterinario().getId() != null) {
            entity.setVeterinario(usuarioJpaRepository.getReferenceById(atencionClinica.getVeterinario().getId()));
        }
        if (atencionClinica.getCreadoPor() != null && atencionClinica.getCreadoPor().getId() != null) {
            entity.setCreadoPor(usuarioJpaRepository.getReferenceById(atencionClinica.getCreadoPor().getId()));
        }
        if (atencionClinica.getActualizadoPor() != null && atencionClinica.getActualizadoPor().getId() != null) {
            entity.setActualizadoPor(usuarioJpaRepository.getReferenceById(atencionClinica.getActualizadoPor().getId()));
        }
        AtencionClinicaEntity saved = atencionClinicaJpaRepository.save(entity);
        return atencionClinicaMapper.toDomain(saved);
    }

    @Override
    public Optional<AtencionClinica> findById(Long id) {
        return atencionClinicaJpaRepository.findById(id).map(atencionClinicaMapper::toDomain);
    }

    @Override
    public Optional<AtencionClinica> findByCitaId(Long citaId) {
        return atencionClinicaJpaRepository.findByCitaId(citaId).map(atencionClinicaMapper::toDomain);
    }

    @Override
    public Page<AtencionClinica> findByMascotaIdOrderByCreadoEnDesc(Long mascotaId, Pageable pageable) {
        return atencionClinicaJpaRepository.findByMascotaIdOrderByCreadoEnDesc(mascotaId, pageable)
                .map(atencionClinicaMapper::toDomain);
    }

    @Override
    public Page<AtencionClinica> findAll(Pageable pageable) {
        return atencionClinicaJpaRepository.findAll(pageable).map(atencionClinicaMapper::toDomain);
    }
}
