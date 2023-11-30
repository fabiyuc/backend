package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TipoGuardiaDto {

    @NotBlank
    private String nombre;
    private String descripcion;

}
