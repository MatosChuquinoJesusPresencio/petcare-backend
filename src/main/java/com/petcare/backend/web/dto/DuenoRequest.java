package com.petcare.backend.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DuenoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        String apellido,

        @NotBlank(message = "El DNI es obligatorio")
        @Size(min = 8, max = 20, message = "El DNI debe tener entre 8 y 20 caracteres")
        String dni,

        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El correo electrónico debe ser válido")
        String email,

        String telefono,
        
        String direccion,
        
        Long usuarioId // opcional, si ya tiene un usuario asociado
) {
}
