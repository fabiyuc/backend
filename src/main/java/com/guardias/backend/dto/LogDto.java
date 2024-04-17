package com.guardias.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {

    @NotNull
    private LocalDate fecha;
    @NotBlank
    private String seccion;
    @NotBlank
    private String accion;
    private boolean activo;
}
