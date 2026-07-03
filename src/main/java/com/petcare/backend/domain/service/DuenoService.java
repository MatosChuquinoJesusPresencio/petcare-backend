package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.ContactoEmergencia;
import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.domain.port.ContactoEmergenciaRepositoryPort;
import com.petcare.backend.domain.port.DuenoRepositoryPort;
import com.petcare.backend.domain.exception.ResourceDuplicateException;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
            throw new ResourceDuplicateException("The owner's DNI is already registered");
        }
        if (dueno.getUsuario() != null && duenoRepositoryPort.findByEmail(dueno.getUsuario().getEmail()).isPresent()) {
            throw new ResourceDuplicateException("The owner's email is already registered");
        }
        return duenoRepositoryPort.save(dueno);
    }

    public Dueno actualizarDueno(Long id, Dueno duenoDetalles) {
        Dueno dueno = duenoRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        duenoRepositoryPort.findByDni(duenoDetalles.getDni()).ifPresent(d -> {
            if (!d.getId().equals(id)) {
                throw new ResourceDuplicateException("DNI already belongs to another owner");
            }
        });

        dueno.setDni(duenoDetalles.getDni());
        dueno.setTelefono(duenoDetalles.getTelefono());
        dueno.setDireccion(duenoDetalles.getDireccion());

        return duenoRepositoryPort.save(dueno);
    }

    public Optional<Dueno> obtenerPorId(Long id) {
        return duenoRepositoryPort.findById(id);
    }

    public Page<Dueno> listar(Boolean soloActivos, String nombre, String dni, Pageable pageable) {
        return duenoRepositoryPort.findAll(soloActivos, nombre, dni, pageable);
    }

    public void desactivarDueno(Long id) {
        duenoRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        duenoRepositoryPort.deleteById(id);
    }

    public void eliminarDueno(Long id) {
        duenoRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        duenoRepositoryPort.deleteById(id);
    }

    public ContactoEmergencia agregarContactoEmergencia(Long duenoId, ContactoEmergencia contacto) {
        Dueno dueno = duenoRepositoryPort.findById(duenoId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        contacto.setDueno(dueno);
        return contactoRepositoryPort.save(contacto);
    }

    public Page<ContactoEmergencia> listarContactosDeDueno(Long duenoId, String nombre, String telefono, String relacion, Pageable pageable) {
        return contactoRepositoryPort.findAll(duenoId, nombre, telefono, relacion, pageable);
    }

    public void eliminarContactoEmergencia(Long contactoId) {
        contactoRepositoryPort.deleteById(contactoId);
    }
}
