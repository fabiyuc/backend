package com.guardias.backend.dto;

import java.util.Set;

import com.guardias.backend.entity.Departamento;
import com.guardias.backend.entity.Efector;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocalidadDto {

    @NotBlank
    private String nombre;

    Departamento departamento;
    private boolean activo;

    private Set<Efector> efectores;

}
