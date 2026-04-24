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
@AllArgsConstructor
@NoArgsConstructor
public class Cirugia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoCirugia;

    private String observaciones;
    
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;

    @ManyToOne
    @JoinColumn(name = "diagnostico_id")
    private Diagnostico diagnostico;
}
