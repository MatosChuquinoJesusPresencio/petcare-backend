package com.petcare.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ControlVacuna {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int dosis;

    private LocalDateTime fechaAplicacion;

    private LocalDateTime fechaProximoRefuerzo;

    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "vacuna_id")
    private Vacuna vacuna;

    @ManyToOne
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;
}
