package com.guardias.backend.dto;

import java.util.Set;

import com.guardias.backend.entity.Efector;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegionDto {

    @NotBlank
    private String nombre;
    private Set<Efector> efectores;
    private boolean activo;
}
