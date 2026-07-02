package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Triaje;
import com.petcare.backend.domain.port.TriajeRepositoryPort;
import com.petcare.backend.persistence.mapper.TriajeMapper;
import com.petcare.backend.persistence.repository.TriajeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TriajeRepositoryAdapter implements TriajeRepositoryPort {

    private final TriajeRepository triajeRepository;
    private final TriajeMapper triajeMapper;

    public TriajeRepositoryAdapter(TriajeRepository triajeRepository, TriajeMapper triajeMapper) {
        this.triajeRepository = triajeRepository;
        this.triajeMapper = triajeMapper;
    }

    @Override
    public Triaje save(Triaje triaje) {
        var entity = triajeMapper.toEntity(triaje);
        var saved = triajeRepository.save(entity);
        return triajeMapper.toModel(saved);
    }

    @Override
    public Optional<Triaje> findById(Long id) {
        return triajeRepository.findById(id).map(triajeMapper::toModel);
    }

    @Override
    public Optional<Triaje> findByCitaId(Long citaId) {
        return triajeRepository.findByCitaId(citaId).map(triajeMapper::toModel);
    }

    @Override
    public Page<Triaje> findAll(Pageable pageable) {
        return triajeRepository.findAll(pageable).map(triajeMapper::toModel);
    }

    @Override
    public Page<Triaje> findByNivelUrgencia(String nivelUrgencia, Pageable pageable) {
        return triajeRepository.findByNivelUrgencia(nivelUrgencia, pageable).map(triajeMapper::toModel);
    }
}
