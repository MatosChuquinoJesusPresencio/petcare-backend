package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.domain.port.ServicioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ServicioService {

    private final ServicioRepositoryPort servicioRepositoryPort;

    public ServicioService(ServicioRepositoryPort servicioRepositoryPort) {
        this.servicioRepositoryPort = servicioRepositoryPort;
    }

    @Transactional
    public Servicio crearServicio(Servicio servicio) {
        servicio.setActivo(true);
        return servicioRepositoryPort.save(servicio);
    }

    @Transactional
    public Servicio actualizarServicio(Long id, Servicio servicioDetalles) {
        Servicio servicio = servicioRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        
        servicio.setNombre(servicioDetalles.getNombre());
        servicio.setDescripcion(servicioDetalles.getDescripcion());
        servicio.setDuracionMinutos(servicioDetalles.getDuracionMinutos());
        servicio.setCostoReferencial(servicioDetalles.getCostoReferencial());
        
        return servicioRepositoryPort.save(servicio);
    }

    public Optional<Servicio> obtenerPorId(Long id) {
        return servicioRepositoryPort.findById(id);
    }

    public Page<Servicio> listar(Boolean soloActivos, String nombre, Pageable pageable) {
        return servicioRepositoryPort.findAll(soloActivos, nombre, pageable);
    }

    @Transactional
    public void eliminarServicio(Long id) {
        servicioRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        servicioRepositoryPort.deleteById(id);
    }

    @Transactional
    public Servicio cambiarActivo(Long id) {
        Servicio servicio = servicioRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        servicio.setActivo(!servicio.getActivo());
        return servicioRepositoryPort.save(servicio);
    }
}
