package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.domain.port.ServicioRepositoryPort;
import com.petcare.backend.persistence.mapper.ServicioMapper;
import com.petcare.backend.persistence.repository.ServicioJpaRepository;
import com.petcare.backend.persistence.specification.ServicioSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ServicioRepositoryAdapter implements ServicioRepositoryPort {

    private final ServicioJpaRepository servicioJpaRepository;
    private final ServicioMapper servicioMapper;

    public ServicioRepositoryAdapter(ServicioJpaRepository servicioJpaRepository, ServicioMapper servicioMapper) {
        this.servicioJpaRepository = servicioJpaRepository;
        this.servicioMapper = servicioMapper;
    }

    @Override
    public Optional<Servicio> findById(Long id) {
        return servicioJpaRepository.findById(id).map(servicioMapper::toDomain);
    }

    @Override
    public Page<Servicio> findAll(Boolean soloActivos, String nombre, Pageable pageable) {
        return servicioJpaRepository.findAll(ServicioSpecification.withFilters(soloActivos, nombre), pageable).map(servicioMapper::toDomain);
    }

    @Override
    public Servicio save(Servicio servicio) {
        return servicioMapper.toDomain(servicioJpaRepository.save(servicioMapper.toEntity(servicio)));
    }

    @Override
    public void deleteById(Long id) {
        servicioJpaRepository.deleteById(id);
    }
}
