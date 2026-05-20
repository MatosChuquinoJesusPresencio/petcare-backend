package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.domain.port.ServicioRepositoryPort;
import com.petcare.backend.persistence.entity.ServicioEntity;
import com.petcare.backend.persistence.mapper.ServicioMapper;
import com.petcare.backend.persistence.repository.ServicioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ServicioRepositoryAdapter implements ServicioRepositoryPort {

    private final ServicioRepository servicioRepository;
    private final ServicioMapper servicioMapper;

    public ServicioRepositoryAdapter(ServicioRepository servicioRepository, ServicioMapper servicioMapper) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
    }

    @Override
    public Optional<Servicio> findById(Long id) {
        return servicioRepository.findById(id).map(servicioMapper::toModel);
    }

    @Override
    public List<Servicio> findAll() {
        return servicioRepository.findAll().stream()
                .map(servicioMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Servicio> findByActivo(Boolean activo) {
        return servicioRepository.findByActivo(activo).stream()
                .map(servicioMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Servicio save(Servicio servicio) {
        ServicioEntity entity = servicioMapper.toEntity(servicio);
        ServicioEntity savedEntity = servicioRepository.save(entity);
        return servicioMapper.toModel(savedEntity);
    }
}
