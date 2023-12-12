package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TipoLicenciaDto {

    @NotBlank
    private String nombre;
    @NotBlank
    private String ley;
    private String articulo;
    private String inciso;
}
