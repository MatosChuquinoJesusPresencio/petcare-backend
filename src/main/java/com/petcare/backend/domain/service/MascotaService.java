package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.model.MascotaResponsable;
import com.petcare.backend.domain.port.DuenoPort;
import com.petcare.backend.domain.port.MascotaPort;
import com.petcare.backend.domain.port.MascotaResponsablePort;
import com.petcare.backend.domain.exception.ResourceDuplicateException;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.domain.exception.BusinessRuleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MascotaService {

    private final MascotaPort mascotaPort;
    private final DuenoPort duenoPort;
    private final MascotaResponsablePort responsablePort;

    public MascotaService(MascotaPort mascotaPort,
                          DuenoPort duenoPort,
                          MascotaResponsablePort responsablePort) {
        this.mascotaPort = mascotaPort;
        this.duenoPort = duenoPort;
        this.responsablePort = responsablePort;
    }

    @Transactional
    public Mascota registrarMascota(Mascota mascota, Long duenoId, String relacion) {
        if (mascota.getMicrochip() != null && !mascota.getMicrochip().trim().isEmpty()) {
            if (mascotaPort.findByMicrochip(mascota.getMicrochip()).isPresent()) {
                throw new ResourceDuplicateException("El microchip de la mascota ya está registrado");
            }
        }

        Dueno dueno = duenoPort.findById(duenoId)
                .orElseThrow(() -> new ResourceNotFoundException("Dueño no encontrado"));

        mascota.setActivo(true);
        Mascota mascotaGuardada = mascotaPort.save(mascota);

        MascotaResponsable responsable = MascotaResponsable.builder()
                .mascota(mascotaGuardada)
                .dueno(dueno)
                .esPrincipal(true)
                .relacion(relacion)
                .build();
        responsablePort.save(responsable);

        return mascotaGuardada;
    }

    @Transactional
    public Mascota actualizarMascota(Long id, Mascota mascotaDetalles) {
        Mascota mascota = mascotaPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        if (mascotaDetalles.getMicrochip() != null && !mascotaDetalles.getMicrochip().trim().isEmpty()) {
            mascotaPort.findByMicrochip(mascotaDetalles.getMicrochip()).ifPresent(m -> {
                if (!m.getId().equals(id)) {
                    throw new ResourceDuplicateException("El microchip ya pertenece a otra mascota");
                }
            });
        }

        mascota.setNombre(mascotaDetalles.getNombre());
        mascota.setEspecie(mascotaDetalles.getEspecie());
        mascota.setRaza(mascotaDetalles.getRaza());
        mascota.setSexo(mascotaDetalles.getSexo());
        mascota.setFechaNacimiento(mascotaDetalles.getFechaNacimiento());
        mascota.setMicrochip(mascotaDetalles.getMicrochip());
        mascota.setCondicionReproductiva(mascotaDetalles.getCondicionReproductiva());
        mascota.setAlergias(mascotaDetalles.getAlergias());
        mascota.setEnfermedadesCronicas(mascotaDetalles.getEnfermedadesCronicas());
        mascota.setAlertasMedicas(mascotaDetalles.getAlertasMedicas());

        return mascotaPort.save(mascota);
    }

    public Optional<Mascota> obtenerPorId(Long id) {
        return mascotaPort.findById(id);
    }

    public List<Mascota> listarTodas() {
        return mascotaPort.findAll();
    }

    @Transactional
    public void desactivarMascota(Long id) {
        Mascota mascota = mascotaPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        mascota.setActivo(false);
        mascotaPort.save(mascota);
    }

    public List<Mascota> listarMascotasDeDueno(Long duenoId) {
        return responsablePort.findByDuenoId(duenoId).stream()
                .map(MascotaResponsable::getMascota)
                .collect(Collectors.toList());
    }

    @Transactional
    public void vincularDueñoAdicional(Long mascotaId, Long duenoId, String relacion) {
        Mascota mascota = mascotaPort.findById(mascotaId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        Dueno dueno = duenoPort.findById(duenoId)
                .orElseThrow(() -> new ResourceNotFoundException("Dueño no encontrado"));

        if (responsablePort.findByMascotaIdAndDuenoId(mascotaId, duenoId).isPresent()) {
            throw new BusinessRuleException("El dueño ya está asociado a esta mascota");
        }

        MascotaResponsable responsable = MascotaResponsable.builder()
                .mascota(mascota)
                .dueno(dueno)
                .esPrincipal(false)
                .relacion(relacion)
                .build();
        responsablePort.save(responsable);
    }
}
