package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.ContactoEmergencia;
import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.domain.port.ContactoEmergenciaPort;
import com.petcare.backend.domain.port.DuenoPort;
import com.petcare.backend.domain.exception.ResourceDuplicateException;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DuenoService {

    private final DuenoPort duenoPort;
    private final ContactoEmergenciaPort contactoPort;

    public DuenoService(DuenoPort duenoPort, ContactoEmergenciaPort contactoPort) {
        this.duenoPort = duenoPort;
        this.contactoPort = contactoPort;
    }

    public Dueno registrarDueno(Dueno dueno) {
        if (duenoPort.findByDni(dueno.getDni()).isPresent()) {
            throw new ResourceDuplicateException("El DNI del dueño ya está registrado");
        }
        if (duenoPort.findByEmail(dueno.getEmail()).isPresent()) {
            throw new ResourceDuplicateException("El correo del dueño ya está registrado");
        }
        dueno.setActivo(true);
        return duenoPort.save(dueno);
    }

    public Dueno actualizarDueno(Long id, Dueno duenoDetalles) {
        Dueno dueno = duenoPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dueño no encontrado"));

        duenoPort.findByDni(duenoDetalles.getDni()).ifPresent(d -> {
            if (!d.getId().equals(id)) {
                throw new ResourceDuplicateException("El DNI ya pertenece a otro dueño");
            }
        });

        duenoPort.findByEmail(duenoDetalles.getEmail()).ifPresent(d -> {
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

        return duenoPort.save(dueno);
    }

    public Optional<Dueno> obtenerPorId(Long id) {
        return duenoPort.findById(id);
    }

    public List<Dueno> listarTodos() {
        return duenoPort.findAll();
    }

    public void desactivarDueno(Long id) {
        Dueno dueno = duenoPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dueño no encontrado"));
        dueno.setActivo(false);
        duenoPort.save(dueno);
    }

    public ContactoEmergencia agregarContactoEmergencia(Long duenoId, ContactoEmergencia contacto) {
        Dueno dueno = duenoPort.findById(duenoId)
                .orElseThrow(() -> new ResourceNotFoundException("Dueño no encontrado"));
        contacto.setDueno(dueno);
        return contactoPort.save(contacto);
    }

    public List<ContactoEmergencia> listarContactosDeDueno(Long duenoId) {
        return contactoPort.findByDuenoId(duenoId);
    }

    public void eliminarContactoEmergencia(Long contactoId) {
        contactoPort.deleteById(contactoId);
    }
}
