package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.HistorialVacunacion;
import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.HistorialVacunacionRepositoryPort;
import com.petcare.backend.domain.port.MascotaRepositoryPort;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class HistorialVacunacionService {

    private final HistorialVacunacionRepositoryPort vacunacionRepositoryPort;
    private final MascotaRepositoryPort mascotaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public HistorialVacunacionService(HistorialVacunacionRepositoryPort vacunacionRepositoryPort,
                                       MascotaRepositoryPort mascotaRepositoryPort,
                                       UsuarioRepositoryPort usuarioRepositoryPort) {
        this.vacunacionRepositoryPort = vacunacionRepositoryPort;
        this.mascotaRepositoryPort = mascotaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Transactional
    public HistorialVacunacion registrar(Long mascotaId, HistorialVacunacion vacunacion,
                                          Long veterinarioId, Long creadoPorId) {
        Mascota mascota = mascotaRepositoryPort.findById(mascotaId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        Usuario veterinario = usuarioRepositoryPort.findById(veterinarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));
        Usuario creador = usuarioRepositoryPort.findById(creadoPorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado"));

        vacunacion.setMascota(mascota);
        vacunacion.setVeterinario(veterinario);
        vacunacion.setCreadoPor(creador);
        if (vacunacion.getEstado() == null) {
            vacunacion.setEstado("APLICADA");
        }

        return vacunacionRepositoryPort.save(vacunacion);
    }

    @Transactional
    public HistorialVacunacion actualizar(Long id, HistorialVacunacion detalles) {
        HistorialVacunacion existente = vacunacionRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de vacunación no encontrado"));

        existente.setTipo(detalles.getTipo());
        existente.setNombreProducto(detalles.getNombreProducto());
        existente.setFechaAplicacion(detalles.getFechaAplicacion());
        existente.setProximaDosis(detalles.getProximaDosis());
        existente.setLote(detalles.getLote());
        existente.setFabricante(detalles.getFabricante());
        existente.setDosis(detalles.getDosis());
        existente.setViaAdministracion(detalles.getViaAdministracion());
        existente.setObservaciones(detalles.getObservaciones());
        existente.setEstado(detalles.getEstado());

        return vacunacionRepositoryPort.save(existente);
    }

    public List<HistorialVacunacion> listarPorMascota(Long mascotaId) {
        return vacunacionRepositoryPort.findByMascotaId(mascotaId);
    }

    public Optional<HistorialVacunacion> obtenerPorId(Long id) {
        return vacunacionRepositoryPort.findById(id);
    }

    public List<HistorialVacunacion> obtenerProximasDosis() {
        LocalDate hoy = LocalDate.now();
        LocalDate enUnaSemana = hoy.plusDays(7);
        return vacunacionRepositoryPort.findByProximaDosisBetween(hoy, enUnaSemana);
    }

    @Transactional
    public void eliminar(Long id) {
        vacunacionRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de vacunación no encontrado"));
        vacunacionRepositoryPort.deleteById(id);
    }
}
