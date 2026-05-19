package com.petcare.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "duenos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DuenoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(nullable = false, unique = true, length = 20)
    private String dni;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(length = 200)
    private String direccion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
