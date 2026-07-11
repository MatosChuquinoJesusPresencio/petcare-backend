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
                .orElseThrow(() -> new ResourceNotFoundException("Horario de disponibilidad no encontrado"));
    }

    @Transactional
    public DisponibilidadVeterinario registrar(DisponibilidadVeterinario disponibilidad) {
        return disponibilidadRepositoryPort.save(disponibilidad);
    }

    @Transactional
    public DisponibilidadVeterinario actualizar(Long id, DisponibilidadVeterinario detalles) {
        if (detalles == null) {
            throw new BusinessRuleException("Los detalles de disponibilidad no deben ser nulos");
        }
        DisponibilidadVeterinario existente = obtenerPorId(id);
        if (detalles.getHoraInicio() == null || detalles.getHoraFin() == null) {
            throw new BusinessRuleException("La hora de inicio y fin no deben ser nulas");
        }
        if (!detalles.getHoraInicio().isBefore(detalles.getHoraFin())) {
            throw new BusinessRuleException("La hora de inicio debe ser anterior a la hora de fin");
        }
        existente.setDiaSemana(detalles.getDiaSemana());
        existente.setHoraInicio(detalles.getHoraInicio());
        existente.setHoraFin(detalles.getHoraFin());
        return disponibilidadRepositoryPort.save(existente);
    }

    @Transactional
    public DisponibilidadVeterinario toggleActivo(Long id) {
        DisponibilidadVeterinario existente = obtenerPorId(id);
        existente.setActivo(!Boolean.TRUE.equals(existente.getActivo()));
        return disponibilidadRepositoryPort.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!disponibilidadRepositoryPort.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Horario de disponibilidad no encontrado");
        }
        disponibilidadRepositoryPort.deleteById(id);
    }
}
