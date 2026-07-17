package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.*;
import com.petcare.backend.domain.port.*;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ConsentimientoService {

    private final ConsentimientoRepositoryPort repositoryPort;
    private final MascotaRepositoryPort mascotaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ConsentimientoService(ConsentimientoRepositoryPort repositoryPort,
                                  MascotaRepositoryPort mascotaRepositoryPort,
                                  UsuarioRepositoryPort usuarioRepositoryPort) {
        this.repositoryPort = repositoryPort;
        this.mascotaRepositoryPort = mascotaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Transactional
    public Consentimiento registrar(Consentimiento consentimiento, Long mascotaId, Long veterinarioId, Long creadoPorId) {
        Mascota mascota = mascotaRepositoryPort.findById(mascotaId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        Usuario veterinario = usuarioRepositoryPort.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));
        Usuario creador = usuarioRepositoryPort.findById(creadoPorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado"));

        consentimiento.setMascota(mascota);
        consentimiento.setVeterinario(veterinario);
        consentimiento.setCreadoPor(creador);
        if (consentimiento.getConsentido() == null) consentimiento.setConsentido(true);

        return repositoryPort.save(consentimiento);
    }

    public List<Consentimiento> listarTodos() {
        return repositoryPort.findAll();
    }

    public List<Consentimiento> listarPorMascota(Long mascotaId) {
        return repositoryPort.findByMascotaId(mascotaId);
    }

    public java.util.Optional<Consentimiento> obtenerPorId(Long id) {
        return repositoryPort.findById(id);
    }
}
