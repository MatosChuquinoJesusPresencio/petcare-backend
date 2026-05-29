package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.domain.port.ServicioRepositoryPort;
import com.petcare.backend.persistence.entity.ServicioEntity;
import com.petcare.backend.persistence.mapper.ServicioMapper;
import com.petcare.backend.persistence.repository.ServicioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
    public Page<Servicio> findAll(Pageable pageable) {
        return servicioRepository.findAll(pageable)
                .map(servicioMapper::toModel);
    }

    @Override
    public Page<Servicio> findByActivo(Boolean activo, Pageable pageable) {
        return servicioRepository.findByActivo(activo, pageable)
                .map(servicioMapper::toModel);
    }

    @Override
    public Servicio save(Servicio servicio) {
        ServicioEntity entity = servicioMapper.toEntity(servicio);
        ServicioEntity savedEntity = servicioRepository.save(entity);
        return servicioMapper.toModel(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        servicioRepository.deleteById(id);
    }
}
