package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.HistorialVacunacion;
import com.petcare.backend.domain.port.HistorialVacunacionRepositoryPort;
import com.petcare.backend.persistence.entity.HistorialVacunacionEntity;
import com.petcare.backend.persistence.mapper.HistorialVacunacionMapper;
import com.petcare.backend.persistence.repository.HistorialVacunacionJpaRepository;
import com.petcare.backend.persistence.repository.MascotaJpaRepository;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class HistorialVacunacionRepositoryAdapter implements HistorialVacunacionRepositoryPort {

    private final HistorialVacunacionJpaRepository jpaRepository;
    private final HistorialVacunacionMapper mapper;
    private final MascotaJpaRepository mascotaJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public HistorialVacunacionRepositoryAdapter(HistorialVacunacionJpaRepository jpaRepository,
                                                 HistorialVacunacionMapper mapper,
                                                 MascotaJpaRepository mascotaJpaRepository,
                                                 UsuarioJpaRepository usuarioJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.mascotaJpaRepository = mascotaJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Optional<HistorialVacunacion> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<HistorialVacunacion> findByMascotaId(Long mascotaId) {
        return jpaRepository.findByMascotaIdOrderByFechaAplicacionDesc(mascotaId)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<HistorialVacunacion> findByProximaDosisBetween(LocalDate desde, LocalDate hasta) {
        return jpaRepository.findByProximaDosisBetweenOrderByProximaDosisAsc(desde, hasta)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public HistorialVacunacion save(HistorialVacunacion vacunacion) {
        HistorialVacunacionEntity entity = mapper.toEntity(vacunacion);
        if (vacunacion.getMascota() != null && vacunacion.getMascota().getId() != null) {
            entity.setMascota(mascotaJpaRepository.getReferenceById(vacunacion.getMascota().getId()));
        }
        if (vacunacion.getVeterinario() != null && vacunacion.getVeterinario().getId() != null) {
            entity.setVeterinario(usuarioJpaRepository.getReferenceById(vacunacion.getVeterinario().getId()));
        }
        if (vacunacion.getCreadoPor() != null && vacunacion.getCreadoPor().getId() != null) {
            entity.setCreadoPor(usuarioJpaRepository.getReferenceById(vacunacion.getCreadoPor().getId()));
        }
        if (vacunacion.getActualizadoPor() != null && vacunacion.getActualizadoPor().getId() != null) {
            entity.setActualizadoPor(usuarioJpaRepository.getReferenceById(vacunacion.getActualizadoPor().getId()));
        }
        HistorialVacunacionEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
