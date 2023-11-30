package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocalidadDto {

    @NotBlank
    private String nombre;

    @NotBlank
    private int idDepartamento;

}
