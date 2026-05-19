package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.domain.port.ServicioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Servicio> listarTodos() {
        return servicioRepositoryPort.findAll();
    }

    public List<Servicio> listarActivos() {
        return servicioRepositoryPort.findByActivo(true);
    }

    public void desactivarServicio(Long id) {
        Servicio servicio = servicioRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        servicio.setActivo(false);
        servicioRepositoryPort.save(servicio);
    }
}
