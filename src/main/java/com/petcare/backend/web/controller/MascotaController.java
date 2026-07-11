package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.MascotaService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.CambioDuenoPrincipalRequest;
import com.petcare.backend.web.dto.request.MascotaRequest;
import com.petcare.backend.web.dto.response.DuenoResponse;
import com.petcare.backend.web.dto.response.MascotaResponse;
import com.petcare.backend.web.dto.response.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;
    private final UsuarioService usuarioService;

    public MascotaController(MascotaService mascotaService, UsuarioService usuarioService) {
        this.mascotaService = mascotaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<Page<MascotaResponse>> listarMascotas(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "especie", required = false) String especie,
            @RequestParam(value = "raza", required = false) String raza,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "duenoId", required = false) Long duenoId,
            Pageable pageable) {
        Page<Mascota> result;
        if (nombre != null || especie != null || raza != null || sexo != null || activo != null || duenoId != null) {
            result = mascotaService.listarTodas(nombre, especie, raza, sexo, activo, duenoId, pageable);
        } else {
            result = mascotaService.listarTodas(pageable);
        }
        return ResponseEntity.ok(result.map(this::toMascotaResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaResponse> obtenerMascota(@PathVariable Long id) {
        return mascotaService.obtenerPorId(id)
                .map(m -> ResponseEntity.ok(toMascotaResponse(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/dueno-principal")
    public ResponseEntity<DuenoResponse> obtenerDuenoPrincipal(@PathVariable Long id) {
        return mascotaService.obtenerDuenoPrincipal(id)
                .map(d -> ResponseEntity.ok(new DuenoResponse(d.getId(), d.getDni(), d.getTelefono(),
                        d.getDireccion(), toUsuarioResponse(d.getUsuario()))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dueno/{duenoId}")
    public ResponseEntity<Page<MascotaResponse>> listarMascotasDeDueno(@PathVariable Long duenoId, Pageable pageable) {
        return ResponseEntity.ok(mascotaService.listarMascotasDeDueno(duenoId, pageable).map(this::toMascotaResponse));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<MascotaResponse> registrarMascota(@Valid @RequestBody MascotaRequest request) {
        Mascota mascota = new Mascota(null, request.name(), request.species(), request.breed(),
                request.gender(), request.birthDate(), request.microchip(),
                request.reproductiveCondition(), request.allergies(), request.chronicDiseases(),
                request.medicalAlerts(), null, null);

        Mascota creada = mascotaService.registrarMascota(mascota, request.ownerId(), request.ownerRelation());
        return new ResponseEntity<>(toMascotaResponse(creada), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<MascotaResponse> actualizarMascota(@PathVariable Long id,
                                                              @Valid @RequestBody MascotaRequest request) {
        Mascota detalles = new Mascota(null, request.name(), request.species(), request.breed(),
                request.gender(), request.birthDate(), request.microchip(),
                request.reproductiveCondition(), request.allergies(), request.chronicDiseases(),
                request.medicalAlerts(), null, null);

        Mascota actualizada = mascotaService.actualizarMascota(id, detalles);
        return ResponseEntity.ok(toMascotaResponse(actualizada));
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<MascotaResponse> cambiarActivo(@PathVariable Long id) {
        mascotaService.cambiarEstado(id);
        Mascota mascota = mascotaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada después del cambio de estado"));
        return ResponseEntity.ok(toMascotaResponse(mascota));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> eliminarMascota(@PathVariable Long id) {
        mascotaService.eliminarMascota(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{mascotaId}/vincular-dueno/{duenoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> vincularDuenoAdicional(@PathVariable Long mascotaId,
                                                        @PathVariable Long duenoId,
                                                        @RequestParam String relacion) {
        mascotaService.vincularDuenoAdicional(mascotaId, duenoId, relacion);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{mascotaId}/cambiar-dueno-principal")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> cambiarDuenoPrincipal(@PathVariable Long mascotaId,
                                                       @Valid @RequestBody CambioDuenoPrincipalRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.obtenerPorEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado"));

        mascotaService.cambiarDuenoPrincipal(mascotaId, request.ownerId(), request.relation(),
                request.reason(), usuario.getId());
        return ResponseEntity.ok().build();
    }

    private MascotaResponse toMascotaResponse(Mascota m) {
        return new MascotaResponse(m.getId(), m.getNombre(), m.getEspecie(), m.getRaza(),
                m.getGenero(), m.getFechaNacimiento(), m.getMicrochip(),
                m.getCondicionReproductiva(), m.getAlergias(), m.getEnfermedadesCronicas(),
                m.getAlertasMedicas(), m.getNotasMedicas(), m.getEstado());
    }

    private UsuarioResponse toUsuarioResponse(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioResponse(usuario.getId(), usuario.getNombres(), usuario.getApellidos(),
                usuario.getEmail(), usuario.getTelefono(), usuario.getRol(), usuario.getEstado());
    }
}
