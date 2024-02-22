package com.guardias.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiraMedicaDto {
    @NotBlank
    private LocalDate fecha;
    @Min(value = 1)
    private int cantidadHoras;
    @NotBlank
    private String descripcion;
    private boolean activo;
}
