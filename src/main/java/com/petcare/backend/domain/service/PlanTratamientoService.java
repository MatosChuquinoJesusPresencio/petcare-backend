package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.*;
import com.petcare.backend.domain.port.*;
import com.petcare.backend.domain.exception.BusinessRuleException;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlanTratamientoService {

    private final PlanTratamientoRepositoryPort planRepositoryPort;
    private final AtencionClinicaRepositoryPort atencionClinicaRepositoryPort;
    private final MascotaRepositoryPort mascotaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public PlanTratamientoService(PlanTratamientoRepositoryPort planRepositoryPort,
                                   AtencionClinicaRepositoryPort atencionClinicaRepositoryPort,
                                   MascotaRepositoryPort mascotaRepositoryPort,
                                   UsuarioRepositoryPort usuarioRepositoryPort) {
        this.planRepositoryPort = planRepositoryPort;
        this.atencionClinicaRepositoryPort = atencionClinicaRepositoryPort;
        this.mascotaRepositoryPort = mascotaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Transactional
    public PlanTratamiento crear(Long atencionClinicaId, PlanTratamiento plan,
                                  Long veterinarioId, Long creadoPorId) {
        var atencion = atencionClinicaRepositoryPort.findById(atencionClinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Atención clínica no encontrada"));
        Mascota mascota = mascotaRepositoryPort.findById(atencion.getMascota().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        Usuario veterinario = usuarioRepositoryPort.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));
        Usuario creador = usuarioRepositoryPort.findById(creadoPorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado"));

        plan.setMascota(mascota);
        plan.setAtencionClinicaId(atencionClinicaId);
        plan.setVeterinario(veterinario);
        plan.setCreadoPor(creador);
        plan.setEstado(plan.getEstado() != null ? plan.getEstado() : "ACTIVO");

        if (plan.getActividades() != null) {
            for (var act : plan.getActividades()) {
                if (act.getEstado() == null) act.setEstado("PENDIENTE");
            }
        }

        return planRepositoryPort.save(plan);
    }

    @Transactional
    public PlanTratamiento actualizar(Long id, PlanTratamiento detalles) {
        PlanTratamiento existente = planRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan de tratamiento no encontrado"));
        existente.setTitulo(detalles.getTitulo());
        existente.setDescripcion(detalles.getDescripcion());
        existente.setFechaInicio(detalles.getFechaInicio());
        existente.setFechaFinEstimada(detalles.getFechaFinEstimada());
        existente.setActividades(detalles.getActividades());
        return planRepositoryPort.save(existente);
    }

    @Transactional
    public PlanTratamiento cambiarEstado(Long id, String nuevoEstado) {
        PlanTratamiento plan = planRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan de tratamiento no encontrado"));
        if (!List.of("ACTIVO", "COMPLETADO", "SUSPENDIDO").contains(nuevoEstado)) {
            throw new BusinessRuleException("Estado inválido");
        }
        plan.setEstado(nuevoEstado);
        return planRepositoryPort.save(plan);
    }

    public Optional<PlanTratamiento> obtenerPorId(Long id) {
        return planRepositoryPort.findById(id);
    }

    public List<PlanTratamiento> listarPorMascota(Long mascotaId) {
        return planRepositoryPort.findByMascotaId(mascotaId);
    }

    public List<PlanTratamiento> listarPorAtencion(Long atencionClinicaId) {
        return planRepositoryPort.findByAtencionClinicaId(atencionClinicaId);
    }
}
