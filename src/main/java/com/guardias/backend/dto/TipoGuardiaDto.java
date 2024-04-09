package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoGuardiaDto {

    @NotBlank
    private String nombre;
    @NotBlank
    private String descripcion;
    private boolean activo;
}