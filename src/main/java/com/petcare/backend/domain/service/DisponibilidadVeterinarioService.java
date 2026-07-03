package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.DisponibilidadVeterinario;
import com.petcare.backend.domain.port.DisponibilidadVeterinarioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.BusinessRuleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DisponibilidadVeterinarioService {

    private final DisponibilidadVeterinarioRepositoryPort disponibilidadRepositoryPort;

    public DisponibilidadVeterinarioService(DisponibilidadVeterinarioRepositoryPort disponibilidadRepositoryPort) {
        this.disponibilidadRepositoryPort = disponibilidadRepositoryPort;
    }

    public List<DisponibilidadVeterinario> listarPorVeterinario(Long veterinarioId) {
        return disponibilidadRepositoryPort.findByVeterinarioId(veterinarioId);
    }

    public DisponibilidadVeterinario obtenerPorId(Long id) {
        return disponibilidadRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability schedule not found"));
    }

    @Transactional
    public DisponibilidadVeterinario registrar(DisponibilidadVeterinario disponibilidad) {
        return disponibilidadRepositoryPort.save(disponibilidad);
    }

    @Transactional
    public DisponibilidadVeterinario actualizar(Long id, DisponibilidadVeterinario detalles) {
        if (detalles == null) {
            throw new BusinessRuleException("Availability details must not be null");
        }
        DisponibilidadVeterinario existente = obtenerPorId(id);
        if (detalles.getHoraInicio() == null || detalles.getHoraFin() == null) {
            throw new BusinessRuleException("Start time and end time must not be null");
        }
        if (!detalles.getHoraInicio().isBefore(detalles.getHoraFin())) {
            throw new BusinessRuleException("Start time must be before end time");
        }
        existente.setDiaSemana(detalles.getDiaSemana());
        existente.setHoraInicio(detalles.getHoraInicio());
        existente.setHoraFin(detalles.getHoraFin());
        return disponibilidadRepositoryPort.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        DisponibilidadVeterinario existente = obtenerPorId(id);
        existente.setActivo(false);
        disponibilidadRepositoryPort.save(existente);
    }
}
