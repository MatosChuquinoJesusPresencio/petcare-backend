package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Servicio;
import com.petcare.backend.domain.port.ServicioPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioService {

    private final ServicioPort servicioPort;

    public ServicioService(ServicioPort servicioPort) {
        this.servicioPort = servicioPort;
    }

    public Servicio crearServicio(Servicio servicio) {
        servicio.setActivo(true);
        return servicioPort.save(servicio);
    }

    public Servicio actualizarServicio(Long id, Servicio servicioDetalles) {
        Servicio servicio = servicioPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        
        servicio.setNombre(servicioDetalles.getNombre());
        servicio.setDescripcion(servicioDetalles.getDescripcion());
        servicio.setDuracionMinutos(servicioDetalles.getDuracionMinutos());
        servicio.setCostoReferencial(servicioDetalles.getCostoReferencial());
        
        return servicioPort.save(servicio);
    }

    public Optional<Servicio> obtenerPorId(Long id) {
        return servicioPort.findById(id);
    }

    public List<Servicio> listarTodos() {
        return servicioPort.findAll();
    }

    public List<Servicio> listarActivos() {
        return servicioPort.findByActivo(true);
    }

    public void desactivarServicio(Long id) {
        Servicio servicio = servicioPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        servicio.setActivo(false);
        servicioPort.save(servicio);
    }
}
