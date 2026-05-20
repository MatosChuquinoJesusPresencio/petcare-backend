package com.petcare.backend.web.controller;

import com.petcare.backend.domain.model.Mascota;
import com.petcare.backend.domain.service.MascotaService;
import com.petcare.backend.web.dto.MascotaRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @GetMapping
    public ResponseEntity<List<Mascota>> listarMascotas() {
        return ResponseEntity.ok(mascotaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtenerMascota(@PathVariable Long id) {
        return mascotaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dueno/{duenoId}")
    public ResponseEntity<List<Mascota>> listarMascotasDeDueno(@PathVariable Long duenoId) {
        return ResponseEntity.ok(mascotaService.listarMascotasDeDueno(duenoId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Mascota> registrarMascota(@Valid @RequestBody MascotaRequest request) {
        Mascota mascota = Mascota.builder()
                .nombre(request.nombre())
                .especie(request.especie())
                .raza(request.raza())
                .sexo(request.sexo())
                .fechaNacimiento(request.fechaNacimiento())
                .microchip(request.microchip())
                .condicionReproductiva(request.condicionReproductiva())
                .alergias(request.alergias())
                .enfermedadesCronicas(request.enfermedadesCronicas())
                .alertasMedicas(request.alertasMedicas())
                .build();

        Mascota creada = mascotaService.registrarMascota(mascota, request.duenoId(), request.relacionDueno());
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Mascota> actualizarMascota(@PathVariable Long id, @Valid @RequestBody MascotaRequest request) {
        Mascota mascotaDetalles = Mascota.builder()
                .nombre(request.nombre())
                .especie(request.especie())
                .raza(request.raza())
                .sexo(request.sexo())
                .fechaNacimiento(request.fechaNacimiento())
                .microchip(request.microchip())
                .condicionReproductiva(request.condicionReproductiva())
                .alergias(request.alergias())
                .enfermedadesCronicas(request.enfermedadesCronicas())
                .alertasMedicas(request.alertasMedicas())
                .build();

        Mascota actualizada = mascotaService.actualizarMascota(id, mascotaDetalles);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> desactivarMascota(@PathVariable Long id) {
        mascotaService.desactivarMascota(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{mascotaId}/vincular-dueno/{duenoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASISTENTE')")
    public ResponseEntity<Void> vincularDueñoAdicional(@PathVariable Long mascotaId, @PathVariable Long duenoId, @RequestParam String relacion) {
        mascotaService.vincularDueñoAdicional(mascotaId, duenoId, relacion);
        return ResponseEntity.ok().build();
    }
}
