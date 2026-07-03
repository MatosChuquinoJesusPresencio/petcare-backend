package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.model.MascotaResponsable;
import com.petcare.backend.domain.port.DuenoRepositoryPort;
import com.petcare.backend.domain.port.MascotaRepositoryPort;
import com.petcare.backend.domain.port.MascotaResponsableRepositoryPort;
import com.petcare.backend.domain.exception.ResourceDuplicateException;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.BusinessRuleException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MascotaService {

    private final MascotaRepositoryPort mascotaRepositoryPort;
    private final DuenoRepositoryPort duenoRepositoryPort;
    private final MascotaResponsableRepositoryPort responsableRepositoryPort;
    private final HistorialTransferenciaService historialTransferenciaService;

    public MascotaService(MascotaRepositoryPort mascotaRepositoryPort,
                          DuenoRepositoryPort duenoRepositoryPort,
                          MascotaResponsableRepositoryPort responsableRepositoryPort,
                          HistorialTransferenciaService historialTransferenciaService) {
        this.mascotaRepositoryPort = mascotaRepositoryPort;
        this.duenoRepositoryPort = duenoRepositoryPort;
        this.responsableRepositoryPort = responsableRepositoryPort;
        this.historialTransferenciaService = historialTransferenciaService;
    }

    @Transactional
    public Mascota registrarMascota(Mascota mascota, Long duenoId, String relacion) {
        if (mascota.getMicrochip() != null && !mascota.getMicrochip().trim().isEmpty()) {
            if (mascotaRepositoryPort.findByMicrochip(mascota.getMicrochip()).isPresent()) {
                throw new ResourceDuplicateException("The pet microchip is already registered");
            }
        }

        Dueno dueno = duenoRepositoryPort.findById(duenoId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        mascota.setEstado(true);
        Mascota mascotaGuardada = mascotaRepositoryPort.save(mascota);

        MascotaResponsable responsable = new MascotaResponsable(
                null, mascotaGuardada, dueno, true, relacion
        );
        responsableRepositoryPort.save(responsable);

        return mascotaGuardada;
    }

    @Transactional
    public Mascota actualizarMascota(Long id, Mascota mascotaDetalles) {
        Mascota mascota = mascotaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

        if (mascotaDetalles.getMicrochip() != null && !mascotaDetalles.getMicrochip().trim().isEmpty()) {
            mascotaRepositoryPort.findByMicrochip(mascotaDetalles.getMicrochip()).ifPresent(m -> {
                if (!m.getId().equals(id)) {
                    throw new ResourceDuplicateException("The microchip already belongs to another pet");
                }
            });
        }

        mascota.setNombre(mascotaDetalles.getNombre());
        mascota.setEspecie(mascotaDetalles.getEspecie());
        mascota.setRaza(mascotaDetalles.getRaza());
        mascota.setGenero(mascotaDetalles.getGenero());
        mascota.setFechaNacimiento(mascotaDetalles.getFechaNacimiento());
        mascota.setMicrochip(mascotaDetalles.getMicrochip());
        mascota.setCondicionReproductiva(mascotaDetalles.getCondicionReproductiva());
        mascota.setAlergias(mascotaDetalles.getAlergias());
        mascota.setEnfermedadesCronicas(mascotaDetalles.getEnfermedadesCronicas());
        mascota.setAlertasMedicas(mascotaDetalles.getAlertasMedicas());
        mascota.setNotasMedicas(mascotaDetalles.getNotasMedicas());

        return mascotaRepositoryPort.save(mascota);
    }

    public Optional<Mascota> obtenerPorId(Long id) {
        return mascotaRepositoryPort.findById(id);
    }

    public Page<Mascota> listarTodas(Pageable pageable) {
        return mascotaRepositoryPort.findAll(pageable);
    }

    public Page<Mascota> listarTodas(String nombre, String especie, String raza, String genero, Boolean estado, Long duenoId, Pageable pageable) {
        return mascotaRepositoryPort.findAll(nombre, especie, raza, genero, estado, duenoId, pageable);
    }

    public Optional<Dueno> obtenerDuenoPrincipal(Long mascotaId) {
        return responsableRepositoryPort.findByMascotaId(mascotaId).stream()
                .filter(mr -> mr.getEsPrincipal())
                .findFirst()
                .map(mr -> mr.getDueno());
    }

    @Transactional
    public void desactivarMascota(Long id) {
        Mascota mascota = mascotaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        mascota.setEstado(false);
        mascotaRepositoryPort.save(mascota);
    }

    @Transactional
    public void cambiarEstado(Long id) {
        Mascota mascota = mascotaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        mascota.setEstado(!Boolean.TRUE.equals(mascota.getEstado()));
        mascotaRepositoryPort.save(mascota);
    }

    @Transactional
    public void eliminarMascota(Long id) {
        mascotaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        mascotaRepositoryPort.deleteById(id);
    }

    public Page<Mascota> listarMascotasDeDueno(Long duenoId, Pageable pageable) {
        return responsableRepositoryPort.findByDuenoId(duenoId, pageable)
                .map(mr -> mr.getMascota());
    }

    @Transactional
    public void vincularDuenoAdicional(Long mascotaId, Long duenoId, String relacion) {
        Mascota mascota = mascotaRepositoryPort.findById(mascotaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        Dueno dueno = duenoRepositoryPort.findById(duenoId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        if (responsableRepositoryPort.findByMascotaIdAndDuenoId(mascotaId, duenoId).isPresent()) {
            throw new BusinessRuleException("The owner is already associated with this pet");
        }

        MascotaResponsable responsable = new MascotaResponsable(
                null, mascota, dueno, false, relacion
        );
        responsableRepositoryPort.save(responsable);
    }

    @Transactional
    public void cambiarDuenoPrincipal(Long mascotaId, Long nuevoDuenoId, String relacion, String motivo, Long usuarioResponsableId) {
        Mascota mascota = mascotaRepositoryPort.findById(mascotaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

        Dueno nuevoDueno = duenoRepositoryPort.findById(nuevoDuenoId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        List<MascotaResponsable> responsables = responsableRepositoryPort.findByMascotaId(mascotaId);

        Long duenoAnteriorId = null;
        for (MascotaResponsable mr : responsables) {
            if (mr.getEsPrincipal()) {
                duenoAnteriorId = mr.getDueno().getId();
                mr.setEsPrincipal(false);
                responsableRepositoryPort.save(mr);
            }
        }

        Optional<MascotaResponsable> existing = responsables.stream()
                .filter(mr -> mr.getDueno().getId().equals(nuevoDuenoId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setEsPrincipal(true);
            responsableRepositoryPort.save(existing.get());
        } else {
            MascotaResponsable nuevo = new MascotaResponsable(
                    null, mascota, nuevoDueno, true, relacion
            );
            responsableRepositoryPort.save(nuevo);
        }

        historialTransferenciaService.registrarTransferencia(mascotaId, duenoAnteriorId, nuevoDuenoId, motivo, usuarioResponsableId);
    }
}
