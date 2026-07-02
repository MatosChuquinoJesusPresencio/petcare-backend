package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.domain.model.Triaje;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.CitaRepositoryPort;
import com.petcare.backend.domain.port.TriajeRepositoryPort;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.BusinessRuleException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TriajeService {

    private final TriajeRepositoryPort triajeRepositoryPort;
    private final CitaRepositoryPort citaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public TriajeService(TriajeRepositoryPort triajeRepositoryPort,
                          CitaRepositoryPort citaRepositoryPort,
                          UsuarioRepositoryPort usuarioRepositoryPort) {
        this.triajeRepositoryPort = triajeRepositoryPort;
        this.citaRepositoryPort = citaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Transactional
    public Triaje crearTriaje(Triaje triaje, Long citaId, Long asistenteId) {
        Cita cita = citaRepositoryPort.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (triajeRepositoryPort.findByCitaId(citaId).isPresent()) {
            throw new BusinessRuleException("This appointment already has a triage evaluation");
        }

        Usuario asistente = usuarioRepositoryPort.findById(asistenteId)
                .orElseThrow(() -> new ResourceNotFoundException("Assistant not found"));

        if (!List.of("ASISTENTE", "ADMINISTRADOR").contains(asistente.getRol().toUpperCase())) {
            throw new BusinessRuleException("Only assistants or administrators can perform triage");
        }

        String urgencia = triaje.getNivelUrgencia().toUpperCase();
        if (!List.of("RUTINARIA", "PREFERENTE", "URGENTE", "EMERGENCIA").contains(urgencia)) {
            throw new BusinessRuleException("Invalid urgency level. Use: RUTINARIA, PREFERENTE, URGENTE, EMERGENCIA");
        }

        triaje.setCita(cita);
        triaje.setNivelUrgencia(urgencia);
        triaje.setAsistente(asistente);

        return triajeRepositoryPort.save(triaje);
    }

    public Page<Triaje> listarTodos(Pageable pageable) {
        return triajeRepositoryPort.findAll(pageable);
    }

    public Page<Triaje> listarPorPrioridad(String nivelUrgencia, Pageable pageable) {
        String urgencia = nivelUrgencia.toUpperCase();
        if (!List.of("RUTINARIA", "PREFERENTE", "URGENTE", "EMERGENCIA").contains(urgencia)) {
            throw new BusinessRuleException("Invalid urgency level");
        }
        return triajeRepositoryPort.findByNivelUrgencia(urgencia, pageable);
    }

    public Triaje obtenerPorId(Long id) {
        return triajeRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Triage not found"));
    }

    public Triaje obtenerPorCitaId(Long citaId) {
        return triajeRepositoryPort.findByCitaId(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Triage not found for this appointment"));
    }
}
