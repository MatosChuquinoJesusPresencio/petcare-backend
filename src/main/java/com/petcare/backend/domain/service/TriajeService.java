package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Cita;
import com.petcare.backend.domain.model.Triaje;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.CitaRepositoryPort;
import com.petcare.backend.domain.port.SalaEsperaRepositoryPort;
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
@Transactional(readOnly = true)
public class TriajeService {

    private final TriajeRepositoryPort triajeRepositoryPort;
    private final CitaRepositoryPort citaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final SalaEsperaRepositoryPort salaEsperaRepositoryPort;

    public TriajeService(TriajeRepositoryPort triajeRepositoryPort,
                          CitaRepositoryPort citaRepositoryPort,
                          UsuarioRepositoryPort usuarioRepositoryPort,
                          SalaEsperaRepositoryPort salaEsperaRepositoryPort) {
        this.triajeRepositoryPort = triajeRepositoryPort;
        this.citaRepositoryPort = citaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.salaEsperaRepositoryPort = salaEsperaRepositoryPort;
    }

    @Transactional
    public Triaje crearTriaje(Triaje triaje, Long citaId, Long asistenteId) {
        Cita cita = citaRepositoryPort.findById(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        if (triajeRepositoryPort.findByCitaId(citaId).isPresent()) {
            throw new BusinessRuleException("Esta cita ya tiene una evaluación de triaje");
        }

        Usuario asistente = usuarioRepositoryPort.findById(asistenteId)
                .orElseThrow(() -> new ResourceNotFoundException("Asistente no encontrado"));

        if (asistente.getRol() == null || !List.of("ASISTENTE", "ADMINISTRADOR", "VETERINARIO").contains(asistente.getRol().toUpperCase())) {
            throw new BusinessRuleException("Solo asistentes o administradores pueden realizar triaje");
        }

        if (triaje.getNivelUrgencia() == null) {
            throw new BusinessRuleException("El nivel de urgencia no debe ser nulo");
        }
        String urgencia = triaje.getNivelUrgencia().toUpperCase();
        if (!List.of("RUTINARIA", "PREFERENTE", "URGENTE", "EMERGENCIA").contains(urgencia)) {
            throw new BusinessRuleException("Nivel de urgencia inválido. Use: RUTINARIA, PREFERENTE, URGENTE, EMERGENCIA");
        }

        triaje.setCita(cita);
        triaje.setNivelUrgencia(urgencia);
        triaje.setAsistente(asistente);

        if (salaEsperaRepositoryPort.findByCitaId(citaId).isEmpty()) {
            throw new BusinessRuleException("La cita debe estar registrada en sala de espera antes del triaje");
        }

        return triajeRepositoryPort.save(triaje);
    }

    public Page<Triaje> listarTodos(Pageable pageable) {
        return triajeRepositoryPort.findAll(pageable);
    }

    public Page<Triaje> listarPorPrioridad(String nivelUrgencia, Pageable pageable) {
        if (nivelUrgencia == null) {
            throw new BusinessRuleException("El nivel de urgencia no debe ser nulo");
        }
        String urgencia = nivelUrgencia.toUpperCase();
        if (!List.of("RUTINARIA", "PREFERENTE", "URGENTE", "EMERGENCIA").contains(urgencia)) {
            throw new BusinessRuleException("Nivel de urgencia inválido");
        }
        return triajeRepositoryPort.findByNivelUrgencia(urgencia, pageable);
    }

    public Triaje obtenerPorId(Long id) {
        return triajeRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Triaje no encontrado"));
    }

    public Triaje obtenerPorCitaId(Long citaId) {
        return triajeRepositoryPort.findByCitaId(citaId)
                .orElseThrow(() -> new ResourceNotFoundException("Triaje no encontrado para esta cita"));
    }
}
