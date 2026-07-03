package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "mascotas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MascotaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String especie;

    @Column(nullable = false, length = 50)
    private String raza;

    @Column(nullable = false, length = 10)
    private String genero;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(unique = true, length = 50)
    private String microchip;

    @Column(name = "condicion_reproductiva", length = 50)
    private String condicionReproductiva;

    @Column(columnDefinition = "TEXT")
    private String alergias;

    @Column(name = "enfermedades_cronicas", columnDefinition = "TEXT")
    private String enfermedadesCronicas;

    @Column(name = "alertas_medicas", columnDefinition = "TEXT")
    private String alertasMedicas;

    @Column(name = "notas_medicas", columnDefinition = "TEXT")
    private String notasMedicas;

    @Column(nullable = false)
    private Boolean estado;

    @PrePersist
    protected void onCreate() {
        this.estado = true;
    }
}
