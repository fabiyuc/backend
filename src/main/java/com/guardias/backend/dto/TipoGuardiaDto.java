package com.guardias.backend.dto;

import com.guardias.backend.enums.TipoGuardiaEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TipoGuardiaDto {

    @NotBlank
    private String nombre;
    private String descripcion;
    @NotNull
    private TipoGuardiaEnum tipoGuardia;
}