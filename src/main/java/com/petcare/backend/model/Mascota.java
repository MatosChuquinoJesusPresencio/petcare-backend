package com.petcare.backend.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String especie;

    private String raza;

    private LocalDateTime fechaNacimiento;

    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MascotaResponsable> responsables;

    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL)
    private List<ControlVacuna> controlesVacuna;

    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL)
    private List<Consulta> consultas;

    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL)
    private List<Cita> citas;
}
