package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.ContactoEmergencia;
import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.domain.port.ContactoEmergenciaRepositoryPort;
import com.petcare.backend.domain.port.DuenoRepositoryPort;
import com.petcare.backend.domain.exception.ResourceDuplicateException;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DuenoService {

    private final DuenoRepositoryPort duenoRepositoryPort;
    private final ContactoEmergenciaRepositoryPort contactoRepositoryPort;

    public DuenoService(DuenoRepositoryPort duenoRepositoryPort, ContactoEmergenciaRepositoryPort contactoRepositoryPort) {
        this.duenoRepositoryPort = duenoRepositoryPort;
        this.contactoRepositoryPort = contactoRepositoryPort;
    }

    public Dueno registrarDueno(Dueno dueno) {
        if (duenoRepositoryPort.findByDni(dueno.getDni()).isPresent()) {
            throw new ResourceDuplicateException("El DNI del dueño ya está registrado");
        }
        if (duenoRepositoryPort.findByEmail(dueno.getEmail()).isPresent()) {
            throw new ResourceDuplicateException("El correo del dueño ya está registrado");
        }
        dueno.setActivo(true);
        return duenoRepositoryPort.save(dueno);
    }

    public Dueno actualizarDueno(Long id, Dueno duenoDetalles) {
        Dueno dueno = duenoRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dueño no encontrado"));

        duenoRepositoryPort.findByDni(duenoDetalles.getDni()).ifPresent(d -> {
            if (!d.getId().equals(id)) {
                throw new ResourceDuplicateException("El DNI ya pertenece a otro dueño");
            }
        });

        duenoRepositoryPort.findByEmail(duenoDetalles.getEmail()).ifPresent(d -> {
            if (!d.getId().equals(id)) {
                throw new ResourceDuplicateException("El correo ya pertenece a otro dueño");
            }
        });

        dueno.setNombre(duenoDetalles.getNombre());
        dueno.setApellido(duenoDetalles.getApellido());
        dueno.setDni(duenoDetalles.getDni());
        dueno.setEmail(duenoDetalles.getEmail());
        dueno.setTelefono(duenoDetalles.getTelefono());
        dueno.setDireccion(duenoDetalles.getDireccion());

        return duenoRepositoryPort.save(dueno);
    }

    public Optional<Dueno> obtenerPorId(Long id) {
        return duenoRepositoryPort.findById(id);
    }

    public List<Dueno> listarTodos() {
        return duenoRepositoryPort.findAll();
    }

    public void desactivarDueno(Long id) {
        Dueno dueno = duenoRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dueño no encontrado"));
        dueno.setActivo(false);
        duenoRepositoryPort.save(dueno);
    }

    public ContactoEmergencia agregarContactoEmergencia(Long duenoId, ContactoEmergencia contacto) {
        Dueno dueno = duenoRepositoryPort.findById(duenoId)
                .orElseThrow(() -> new ResourceNotFoundException("Dueño no encontrado"));
        contacto.setDueno(dueno);
        return contactoRepositoryPort.save(contacto);
    }

    public List<ContactoEmergencia> listarContactosDeDueno(Long duenoId) {
        return contactoRepositoryPort.findByDuenoId(duenoId);
    }

    public void eliminarContactoEmergencia(Long contactoId) {
        contactoRepositoryPort.deleteById(contactoId);
    }
}
