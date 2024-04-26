package com.guardias.backend.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {

    @NotBlank
    private LocalDateTime fecha;
    @NotBlank
    private String seccion;
    @NotBlank
    private String accion;
    private boolean activo;
}
