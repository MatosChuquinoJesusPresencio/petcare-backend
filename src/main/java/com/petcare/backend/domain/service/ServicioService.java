package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.domain.port.ServicioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class ServicioService {

    private final ServicioRepositoryPort servicioRepositoryPort;

    public ServicioService(ServicioRepositoryPort servicioRepositoryPort) {
        this.servicioRepositoryPort = servicioRepositoryPort;
    }

    public Servicio crearServicio(Servicio servicio) {
        servicio.setActivo(true);
        return servicioRepositoryPort.save(servicio);
    }

    public Servicio actualizarServicio(Long id, Servicio servicioDetalles) {
        Servicio servicio = servicioRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        
        servicio.setNombre(servicioDetalles.getNombre());
        servicio.setDescripcion(servicioDetalles.getDescripcion());
        servicio.setDuracionMinutos(servicioDetalles.getDuracionMinutos());
        servicio.setCostoReferencial(servicioDetalles.getCostoReferencial());
        
        return servicioRepositoryPort.save(servicio);
    }

    public Optional<Servicio> obtenerPorId(Long id) {
        return servicioRepositoryPort.findById(id);
    }

    public Page<Servicio> listarTodos(Pageable pageable) {
        return servicioRepositoryPort.findAll(pageable);
    }

    public Page<Servicio> listarActivos(Pageable pageable) {
        return servicioRepositoryPort.findByActivo(true, pageable);
    }

    public void eliminarServicio(Long id) {
        servicioRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        servicioRepositoryPort.deleteById(id);
    }

    public Servicio cambiarActivo(Long id) {
        Servicio servicio = servicioRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        servicio.setActivo(!servicio.getActivo());
        return servicioRepositoryPort.save(servicio);
    }
}
