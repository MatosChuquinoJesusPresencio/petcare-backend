package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.service.MascotaService;
import com.petcare.backend.web.dto.MascotaRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @GetMapping
    public ResponseEntity<Page<Mascota>> listarMascotas(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "especie", required = false) String especie,
            @RequestParam(value = "raza", required = false) String raza,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "activo", required = false) Boolean activo,
            @RequestParam(value = "duenoId", required = false) Long duenoId,
            Pageable pageable) {
        if (nombre != null || especie != null || raza != null || sexo != null || activo != null || duenoId != null) {
            return ResponseEntity.ok(mascotaService.listarTodas(nombre, especie, raza, sexo, activo, duenoId, pageable));
        }
        return ResponseEntity.ok(mascotaService.listarTodas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtenerMascota(@PathVariable Long id) {
        return mascotaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dueno/{duenoId}")
    public ResponseEntity<Page<Mascota>> listarMascotasDeDueno(@PathVariable Long duenoId, Pageable pageable) {
        return ResponseEntity.ok(mascotaService.listarMascotasDeDueno(duenoId, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Mascota> registrarMascota(@Valid @RequestBody MascotaRequest request) {
        Mascota mascota = Mascota.builder()
                .nombre(request.name())
                .especie(request.species())
                .raza(request.breed())
                .sexo(request.gender())
                .fechaNacimiento(request.birthDate())
                .microchip(request.microchip())
                .condicionReproductiva(request.reproductiveCondition())
                .alergias(request.allergies())
                .enfermedadesCronicas(request.chronicDiseases())
                .alertasMedicas(request.medicalAlerts())
                .build();

        Mascota creada = mascotaService.registrarMascota(mascota, request.ownerId(), request.ownerRelation());
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Mascota> actualizarMascota(@PathVariable Long id, @Valid @RequestBody MascotaRequest request) {
        Mascota mascotaDetalles = Mascota.builder()
                .nombre(request.name())
                .especie(request.species())
                .raza(request.breed())
                .sexo(request.gender())
                .fechaNacimiento(request.birthDate())
                .microchip(request.microchip())
                .condicionReproductiva(request.reproductiveCondition())
                .alergias(request.allergies())
                .enfermedadesCronicas(request.chronicDiseases())
                .alertasMedicas(request.medicalAlerts())
                .build();

        Mascota actualizada = mascotaService.actualizarMascota(id, mascotaDetalles);
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Mascota> cambiarActivo(@PathVariable Long id) {
        Mascota mascota = mascotaService.cambiarActivo(id);
        return ResponseEntity.ok(mascota);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> eliminarMascota(@PathVariable Long id) {
        mascotaService.eliminarMascota(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{mascotaId}/vincular-dueno/{duenoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> vincularDueñoAdicional(@PathVariable Long mascotaId, @PathVariable Long duenoId, @RequestParam String relacion) {
        mascotaService.vincularDueñoAdicional(mascotaId, duenoId, relacion);
        return ResponseEntity.ok().build();
    }
}
