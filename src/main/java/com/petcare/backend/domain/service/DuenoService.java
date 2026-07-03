package com.petcare.backend.domain.service;

import com.petcare.backend.domain.model.ContactoEmergencia;
import com.petcare.backend.domain.model.Dueno;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.port.ContactoEmergenciaRepositoryPort;
import com.petcare.backend.domain.port.DuenoRepositoryPort;
import com.petcare.backend.domain.port.UsuarioRepositoryPort;
import com.petcare.backend.domain.exception.ResourceDuplicateException;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DuenoService {

    private final DuenoRepositoryPort duenoRepositoryPort;
    private final ContactoEmergenciaRepositoryPort contactoRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public DuenoService(DuenoRepositoryPort duenoRepositoryPort, ContactoEmergenciaRepositoryPort contactoRepositoryPort, UsuarioRepositoryPort usuarioRepositoryPort) {
        this.duenoRepositoryPort = duenoRepositoryPort;
        this.contactoRepositoryPort = contactoRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Transactional
    public Dueno registrarDueno(Dueno dueno) {
        if (duenoRepositoryPort.findByDni(dueno.getDni()).isPresent()) {
            throw new ResourceDuplicateException("The owner's DNI is already registered");
        }
        if (dueno.getUsuario() != null && duenoRepositoryPort.findByEmail(dueno.getUsuario().getEmail()).isPresent()) {
            throw new ResourceDuplicateException("The owner's email is already registered");
        }
        return duenoRepositoryPort.save(dueno);
    }

    @Transactional
    public Dueno actualizarDueno(Long id, Dueno duenoDetalles) {
        Dueno dueno = duenoRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        if (duenoDetalles.getDni() != null) {
            String dni = duenoDetalles.getDni();
            duenoRepositoryPort.findByDni(dni).ifPresent(d -> {
                if (!d.getId().equals(id)) {
                    throw new ResourceDuplicateException("DNI already belongs to another owner");
                }
            });
        }

        if (duenoDetalles.getDni() != null) {
            dueno.setDni(duenoDetalles.getDni());
        }
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

    @Transactional
    public void desactivarDueno(Long id) {
        Dueno dueno = duenoRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        Usuario usuario = dueno.getUsuario();
        if (usuario != null) {
            usuario.setEstado(false);
            usuarioRepositoryPort.save(usuario);
        }
    }

    @Transactional
    public void eliminarDueno(Long id) {
        duenoRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        duenoRepositoryPort.deleteById(id);
    }

    @Transactional
    public ContactoEmergencia agregarContactoEmergencia(Long duenoId, ContactoEmergencia contacto) {
        Dueno dueno = duenoRepositoryPort.findById(duenoId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        contacto.setDueno(dueno);
        return contactoRepositoryPort.save(contacto);
    }

    public Page<ContactoEmergencia> listarContactosDeDueno(Long duenoId, String nombre, String telefono, String relacion, Pageable pageable) {
        return contactoRepositoryPort.findAll(duenoId, nombre, telefono, relacion, pageable);
    }

    @Transactional
    public void eliminarContactoEmergencia(Long contactoId) {
        contactoRepositoryPort.findById(contactoId)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency contact not found"));
        contactoRepositoryPort.deleteById(contactoId);
    }
}
