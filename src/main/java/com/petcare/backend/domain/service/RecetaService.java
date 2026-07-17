package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.*;
import com.petcare.backend.domain.port.AtencionClinicaRepositoryPort;
import com.petcare.backend.domain.port.MascotaRepositoryPort;
import com.petcare.backend.domain.port.RecetaRepositoryPort;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RecetaService {

    private final RecetaRepositoryPort recetaRepositoryPort;
    private final AtencionClinicaRepositoryPort atencionClinicaRepositoryPort;
    private final MascotaRepositoryPort mascotaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public RecetaService(RecetaRepositoryPort recetaRepositoryPort,
                          AtencionClinicaRepositoryPort atencionClinicaRepositoryPort,
                          MascotaRepositoryPort mascotaRepositoryPort,
                          UsuarioRepositoryPort usuarioRepositoryPort) {
        this.recetaRepositoryPort = recetaRepositoryPort;
        this.atencionClinicaRepositoryPort = atencionClinicaRepositoryPort;
        this.mascotaRepositoryPort = mascotaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Transactional
    public Receta crear(Long atencionClinicaId, Receta receta, Long veterinarioId, Long creadoPorId) {
        var atencion = atencionClinicaRepositoryPort.findById(atencionClinicaId)
                .orElseThrow(() -> new ResourceNotFoundException("Atención clínica no encontrada"));
        Mascota mascota = mascotaRepositoryPort.findById(atencion.getMascota().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        Usuario veterinario = usuarioRepositoryPort.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));
        Usuario creador = usuarioRepositoryPort.findById(creadoPorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado"));

        receta.setAtencionClinicaId(atencionClinicaId);
        receta.setMascota(mascota);
        receta.setVeterinario(veterinario);
        receta.setCreadoPor(creador);
        receta.setEstado("ACTIVA");

        return recetaRepositoryPort.save(receta);
    }

    @Transactional
    public Receta actualizar(Long id, Receta detalles) {
        Receta existente = recetaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada"));
        existente.setDiagnostico(detalles.getDiagnostico());
        existente.setNotasAdicionales(detalles.getNotasAdicionales());
        existente.setDetalles(detalles.getDetalles());
        return recetaRepositoryPort.save(existente);
    }

    public Optional<Receta> obtenerPorId(Long id) {
        return recetaRepositoryPort.findById(id);
    }

    public List<Receta> listarPorAtencion(Long atencionClinicaId) {
        return recetaRepositoryPort.findByAtencionClinicaId(atencionClinicaId);
    }

    public List<Receta> listarPorMascota(Long mascotaId) {
        return recetaRepositoryPort.findByMascotaId(mascotaId);
    }
}
