package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.domain.port.CitaRepositoryPort;
import com.petcare.backend.persistence.entity.CitaEntity;
import com.petcare.backend.persistence.mapper.CitaMapper;
import com.petcare.backend.persistence.repository.CitaJpaRepository;
import com.petcare.backend.persistence.repository.MascotaJpaRepository;
import com.petcare.backend.persistence.repository.ServicioJpaRepository;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import com.petcare.backend.persistence.specification.CitaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class CitaRepositoryAdapter implements CitaRepositoryPort {

    private final CitaJpaRepository citaJpaRepository;
    private final CitaMapper citaMapper;
    private final MascotaJpaRepository mascotaJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final ServicioJpaRepository servicioJpaRepository;

    public CitaRepositoryAdapter(CitaJpaRepository citaJpaRepository,
                                 CitaMapper citaMapper,
                                 MascotaJpaRepository mascotaJpaRepository,
                                 UsuarioJpaRepository usuarioJpaRepository,
                                 ServicioJpaRepository servicioJpaRepository) {
        this.citaJpaRepository = citaJpaRepository;
        this.citaMapper = citaMapper;
        this.mascotaJpaRepository = mascotaJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.servicioJpaRepository = servicioJpaRepository;
    }

    @Override
    public Optional<Cita> findById(Long id) {
        return citaJpaRepository.findById(id).map(citaMapper::toDomain);
    }

    @Override
    public Page<Cita> findAll(Pageable pageable) {
        return citaJpaRepository.findAll(pageable).map(citaMapper::toDomain);
    }

    @Override
    public Page<Cita> findAll(Long mascotaId, Long veterinarioId, Long servicioId, String estado,
                              Instant fechaDesde, Instant fechaHasta, Pageable pageable) {
        return citaJpaRepository.findAll(CitaSpecification.withFilters(mascotaId, veterinarioId, servicioId, estado, fechaDesde, fechaHasta), pageable)
                .map(citaMapper::toDomain);
    }

    @Override
    public Page<Cita> findByMascotaId(Long mascotaId, Pageable pageable) {
        return citaJpaRepository.findByMascotaId(mascotaId, pageable).map(citaMapper::toDomain);
    }

    @Override
    public Page<Cita> findByVeterinarioId(Long veterinarioId, Pageable pageable) {
        return citaJpaRepository.findByVeterinarioId(veterinarioId, pageable).map(citaMapper::toDomain);
    }

    @Override
    public List<Cita> findByVeterinarioIdAndFechaHoraBetween(Long veterinarioId, Instant start, Instant end) {
        return citaJpaRepository.findByVeterinarioIdAndFechaHoraBetweenOrderByFechaHoraAsc(veterinarioId, start, end).stream()
                .map(citaMapper::toDomain).toList();
    }

    @Override
    public Cita save(Cita cita) {
        CitaEntity entity = citaMapper.toEntity(cita);
        if (cita.getMascota() != null && cita.getMascota().getId() != null) {
            entity.setMascota(mascotaJpaRepository.getReferenceById(cita.getMascota().getId()));
        }
        if (cita.getVeterinario() != null && cita.getVeterinario().getId() != null) {
            entity.setVeterinario(usuarioJpaRepository.getReferenceById(cita.getVeterinario().getId()));
        }
        if (cita.getServicio() != null && cita.getServicio().getId() != null) {
            entity.setServicio(servicioJpaRepository.getReferenceById(cita.getServicio().getId()));
        }
        if (cita.getCreadoPor() != null && cita.getCreadoPor().getId() != null) {
            entity.setCreadoPor(usuarioJpaRepository.getReferenceById(cita.getCreadoPor().getId()));
        }
        if (cita.getActualizadoPor() != null && cita.getActualizadoPor().getId() != null) {
            entity.setActualizadoPor(usuarioJpaRepository.getReferenceById(cita.getActualizadoPor().getId()));
        }
        CitaEntity saved = citaJpaRepository.save(entity);
        return citaMapper.toDomain(saved);
    }
}
