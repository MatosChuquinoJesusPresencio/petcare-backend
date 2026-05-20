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

import java.util.Optional;

@Service
public class MascotaService {

    private final MascotaRepositoryPort mascotaRepositoryPort;
    private final DuenoRepositoryPort duenoRepositoryPort;
    private final MascotaResponsableRepositoryPort responsableRepositoryPort;

    public MascotaService(MascotaRepositoryPort mascotaRepositoryPort,
                          DuenoRepositoryPort duenoRepositoryPort,
                          MascotaResponsableRepositoryPort responsableRepositoryPort) {
        this.mascotaRepositoryPort = mascotaRepositoryPort;
        this.duenoRepositoryPort = duenoRepositoryPort;
        this.responsableRepositoryPort = responsableRepositoryPort;
    }

    @Transactional
    public Mascota registrarMascota(Mascota mascota, Long duenoId, String relacion) {
        if (mascota.getMicrochip() != null && !mascota.getMicrochip().trim().isEmpty()) {
            if (mascotaRepositoryPort.findByMicrochip(mascota.getMicrochip()).isPresent()) {
                throw new ResourceDuplicateException("El microchip de la mascota ya está registrado");
            }
        }

        Dueno dueno = duenoRepositoryPort.findById(duenoId)
                .orElseThrow(() -> new ResourceNotFoundException("Dueño no encontrado"));

        mascota.setActivo(true);
        Mascota mascotaGuardada = mascotaRepositoryPort.save(mascota);

        MascotaResponsable responsable = MascotaResponsable.builder()
                .mascota(mascotaGuardada)
                .dueno(dueno)
                .esPrincipal(true)
                .relacion(relacion)
                .build();
        responsableRepositoryPort.save(responsable);

        return mascotaGuardada;
    }

    @Transactional
    public Mascota actualizarMascota(Long id, Mascota mascotaDetalles) {
        Mascota mascota = mascotaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        if (mascotaDetalles.getMicrochip() != null && !mascotaDetalles.getMicrochip().trim().isEmpty()) {
            mascotaRepositoryPort.findByMicrochip(mascotaDetalles.getMicrochip()).ifPresent(m -> {
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

        return mascotaRepositoryPort.save(mascota);
    }

    public Optional<Mascota> obtenerPorId(Long id) {
        return mascotaRepositoryPort.findById(id);
    }

    public Page<Mascota> listarTodas(Pageable pageable) {
        return mascotaRepositoryPort.findAll(pageable);
    }

    @Transactional
    public void desactivarMascota(Long id) {
        Mascota mascota = mascotaRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        mascota.setActivo(false);
        mascotaRepositoryPort.save(mascota);
    }

    public Page<Mascota> listarMascotasDeDueno(Long duenoId, Pageable pageable) {
        return responsableRepositoryPort.findByDuenoId(duenoId, pageable)
                .map(MascotaResponsable::getMascota);
    }

    @Transactional
    public void vincularDueñoAdicional(Long mascotaId, Long duenoId, String relacion) {
        Mascota mascota = mascotaRepositoryPort.findById(mascotaId)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        Dueno dueno = duenoRepositoryPort.findById(duenoId)
                .orElseThrow(() -> new ResourceNotFoundException("Dueño no encontrado"));

        if (responsableRepositoryPort.findByMascotaIdAndDuenoId(mascotaId, duenoId).isPresent()) {
            throw new BusinessRuleException("El dueño ya está asociado a esta mascota");
        }

        MascotaResponsable responsable = MascotaResponsable.builder()
                .mascota(mascota)
                .dueno(dueno)
                .esPrincipal(false)
                .relacion(relacion)
                .build();
        responsableRepositoryPort.save(responsable);
    }
}
