package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.model.Usuario;
import com.petcare.backend.domain.service.AuditoriaService;
import com.petcare.backend.domain.service.MascotaService;
import com.petcare.backend.domain.service.UsuarioService;
import com.petcare.backend.domain.exception.ResourceNotFoundException;
import com.petcare.backend.web.dto.request.CambioDuenoPrincipalRequest;
import com.petcare.backend.web.dto.request.MascotaRequest;
import com.petcare.backend.web.dto.response.DuenoResponse;
import com.petcare.backend.web.dto.response.MascotaAlertaResponse;
import com.petcare.backend.web.dto.response.MascotaResponse;
import com.petcare.backend.web.dto.response.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;
    private final UsuarioService usuarioService;
    private final AuditoriaService auditoriaService;

    public MascotaController(MascotaService mascotaService, UsuarioService usuarioService,
                             AuditoriaService auditoriaService) {
        this.mascotaService = mascotaService;
        this.usuarioService = usuarioService;
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public ResponseEntity<Page<MascotaResponse>> listarMascotas(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "especie", required = false) String especie,
            @RequestParam(value = "raza", required = false) String raza,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "duenoId", required = false) Long duenoId,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
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
    public ResponseEntity<Page<MascotaResponse>> listarMascotasDeDueno(@PathVariable Long duenoId, @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
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

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            java.util.LinkedHashMap<String, String> campos = new java.util.LinkedHashMap<>();
            campos.put("nombre", creada.getNombre());
            campos.put("especie", creada.getEspecie());
            campos.put("raza", creada.getRaza());
            campos.put("genero", creada.getGenero());
            if (creada.getFechaNacimiento() != null) campos.put("fechaNacimiento", String.valueOf(creada.getFechaNacimiento()));
            campos.put("microchip", creada.getMicrochip());
            campos.put("alergias", creada.getAlergias());
            campos.put("enfermedadesCronicas", creada.getEnfermedadesCronicas());
            campos.put("alertasMedicas", creada.getAlertasMedicas());
            campos.put("notasMedicas", creada.getNotasMedicas());
            auditoriaService.registrarCreacion("mascotas", creada.getId(), auditor, campos);
        } catch (Exception ignored) { }

        return new ResponseEntity<>(toMascotaResponse(creada), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<MascotaResponse> actualizarMascota(@PathVariable Long id,
                                                              @Valid @RequestBody MascotaRequest request) {
        Mascota existente = mascotaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        Mascota detalles = new Mascota(null, request.name(), request.species(), request.breed(),
                request.gender(), request.birthDate(), request.microchip(),
                request.reproductiveCondition(), request.allergies(), request.chronicDiseases(),
                request.medicalAlerts(), null, null);

        Mascota actualizada = mascotaService.actualizarMascota(id, detalles);

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            java.util.LinkedHashMap<String, String[]> cambios = new java.util.LinkedHashMap<>();
            if (!str(existente.getNombre()).equals(str(actualizada.getNombre())))
                cambios.put("nombre", new String[]{str(existente.getNombre()), str(actualizada.getNombre())});
            if (!str(existente.getEspecie()).equals(str(actualizada.getEspecie())))
                cambios.put("especie", new String[]{str(existente.getEspecie()), str(actualizada.getEspecie())});
            if (!str(existente.getRaza()).equals(str(actualizada.getRaza())))
                cambios.put("raza", new String[]{str(existente.getRaza()), str(actualizada.getRaza())});
            if (!str(existente.getGenero()).equals(str(actualizada.getGenero())))
                cambios.put("genero", new String[]{str(existente.getGenero()), str(actualizada.getGenero())});
            if (!str(existente.getFechaNacimiento()).equals(str(actualizada.getFechaNacimiento())))
                cambios.put("fechaNacimiento", new String[]{str(existente.getFechaNacimiento()), str(actualizada.getFechaNacimiento())});
            if (!str(existente.getMicrochip()).equals(str(actualizada.getMicrochip())))
                cambios.put("microchip", new String[]{str(existente.getMicrochip()), str(actualizada.getMicrochip())});
            if (!str(existente.getCondicionReproductiva()).equals(str(actualizada.getCondicionReproductiva())))
                cambios.put("condicionReproductiva", new String[]{str(existente.getCondicionReproductiva()), str(actualizada.getCondicionReproductiva())});
            if (!str(existente.getAlergias()).equals(str(actualizada.getAlergias())))
                cambios.put("alergias", new String[]{str(existente.getAlergias()), str(actualizada.getAlergias())});
            if (!str(existente.getEnfermedadesCronicas()).equals(str(actualizada.getEnfermedadesCronicas())))
                cambios.put("enfermedadesCronicas", new String[]{str(existente.getEnfermedadesCronicas()), str(actualizada.getEnfermedadesCronicas())});
            if (!str(existente.getAlertasMedicas()).equals(str(actualizada.getAlertasMedicas())))
                cambios.put("alertasMedicas", new String[]{str(existente.getAlertasMedicas()), str(actualizada.getAlertasMedicas())});
            if (!str(existente.getNotasMedicas()).equals(str(actualizada.getNotasMedicas())))
                cambios.put("notasMedicas", new String[]{str(existente.getNotasMedicas()), str(actualizada.getNotasMedicas())});
            if (!str(existente.getEstado()).equals(str(actualizada.getEstado())))
                cambios.put("estado", new String[]{str(existente.getEstado()), str(actualizada.getEstado())});
            if (!cambios.isEmpty()) {
                auditoriaService.registrarEdicion("mascotas", id, auditor, cambios, "Actualización de mascota");
            }
        } catch (Exception ignored) { }

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
        Mascota existente = mascotaService.obtenerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
        mascotaService.eliminarMascota(id);

        try {
            Usuario auditor = obtenerUsuarioAutenticado();
            auditoriaService.registrarEliminacion("mascotas", id, auditor, Map.of(
                    "nombre", existente.getNombre() != null ? existente.getNombre() : "",
                    "especie", existente.getEspecie() != null ? existente.getEspecie() : ""
            ));
        } catch (Exception ignored) { }

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

    @GetMapping("/{id}/alertas-medicas")
    public ResponseEntity<MascotaAlertaResponse> obtenerAlertasMedicas(@PathVariable Long id) {
        return mascotaService.obtenerPorId(id)
                .map(m -> ResponseEntity.ok(toAlertaResponse(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/alertas-activas")
    public ResponseEntity<List<MascotaAlertaResponse>> listarMascotasConAlertas() {
        Page<Mascota> todas = mascotaService.listarTodas(org.springframework.data.domain.PageRequest.of(0, 1000));
        List<MascotaAlertaResponse> conAlertas = new ArrayList<>();
        for (Mascota m : todas.getContent()) {
            if (tieneAlertas(m)) {
                conAlertas.add(toAlertaResponse(m));
            }
        }
        return ResponseEntity.ok(conAlertas);
    }

    private MascotaResponse toMascotaResponse(Mascota m) {
        return new MascotaResponse(m.getId(), m.getNombre(), m.getEspecie(), m.getRaza(),
                m.getGenero(), m.getFechaNacimiento(), m.getMicrochip(),
                m.getCondicionReproductiva(), m.getAlergias(), m.getEnfermedadesCronicas(),
                m.getAlertasMedicas(), m.getNotasMedicas(), m.getEstado());
    }

    private MascotaAlertaResponse toAlertaResponse(Mascota m) {
        boolean tiene = tieneAlertas(m);
        return new MascotaAlertaResponse(
                m.getId(), m.getNombre(), m.getEspecie(), m.getRaza(),
                m.getAlergias(), m.getEnfermedadesCronicas(), m.getAlertasMedicas(),
                m.getNotasMedicas(), tiene
        );
    }

    private boolean tieneAlertas(Mascota m) {
        return (m.getAlergias() != null && !m.getAlergias().isBlank())
                || (m.getEnfermedadesCronicas() != null && !m.getEnfermedadesCronicas().isBlank())
                || (m.getAlertasMedicas() != null && !m.getAlertasMedicas().isBlank())
                || (m.getNotasMedicas() != null && !m.getNotasMedicas().isBlank());
    }

    private UsuarioResponse toUsuarioResponse(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioResponse(usuario.getId(), usuario.getNombres(), usuario.getApellidos(),
                usuario.getEmail(), usuario.getTelefono(), usuario.getRol(), usuario.getEstado());
    }

    private Usuario obtenerUsuarioAutenticado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioService.obtenerPorEmail(username).orElse(null);
    }

    private String str(Object obj) {
        return obj != null ? String.valueOf(obj) : "";
    }
}
