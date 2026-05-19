package com.petcare.backend.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RefreshToken {
    private Long id;
    private Usuario usuario;
    private String token;
    private LocalDateTime fechaExpiracion;
}
