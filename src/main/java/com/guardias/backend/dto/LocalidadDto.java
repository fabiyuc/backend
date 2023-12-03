package com.guardias.backend.dto;

import com.guardias.backend.entity.Departamento;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocalidadDto {

    @NotBlank
    private String nombre;

    Departamento departamento;

}
