package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Triaje;
import com.petcare.backend.domain.port.TriajeRepositoryPort;
import com.petcare.backend.persistence.entity.TriajeEntity;
import com.petcare.backend.persistence.mapper.TriajeMapper;
import com.petcare.backend.persistence.repository.CitaJpaRepository;
import com.petcare.backend.persistence.repository.TriajeJpaRepository;
import com.petcare.backend.persistence.repository.UsuarioJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TriajeRepositoryAdapter implements TriajeRepositoryPort {

    private final TriajeJpaRepository triajeJpaRepository;
    private final TriajeMapper triajeMapper;
    private final CitaJpaRepository citaJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;

    public TriajeRepositoryAdapter(TriajeJpaRepository triajeJpaRepository,
                                   TriajeMapper triajeMapper,
                                   CitaJpaRepository citaJpaRepository,
                                   UsuarioJpaRepository usuarioJpaRepository) {
        this.triajeJpaRepository = triajeJpaRepository;
        this.triajeMapper = triajeMapper;
        this.citaJpaRepository = citaJpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public Triaje save(Triaje triaje) {
        TriajeEntity entity = triajeMapper.toEntity(triaje);
        if (triaje.getCita() != null && triaje.getCita().getId() != null) {
            entity.setCita(citaJpaRepository.getReferenceById(triaje.getCita().getId()));
        }
        if (triaje.getAsistente() != null && triaje.getAsistente().getId() != null) {
            entity.setAsistente(usuarioJpaRepository.getReferenceById(triaje.getAsistente().getId()));
        }
        TriajeEntity saved = triajeJpaRepository.save(entity);
        return triajeMapper.toDomain(saved);
    }

    @Override
    public Optional<Triaje> findById(Long id) {
        return triajeJpaRepository.findById(id).map(triajeMapper::toDomain);
    }

    @Override
    public Optional<Triaje> findByCitaId(Long citaId) {
        return triajeJpaRepository.findByCitaId(citaId).map(triajeMapper::toDomain);
    }

    @Override
    public Page<Triaje> findAll(Pageable pageable) {
        return triajeJpaRepository.findAll(pageable).map(triajeMapper::toDomain);
    }

    @Override
    public Page<Triaje> findByNivelUrgencia(String nivelUrgencia, Pageable pageable) {
        return triajeJpaRepository.findByNivelUrgencia(nivelUrgencia, pageable).map(triajeMapper::toDomain);
    }
}
