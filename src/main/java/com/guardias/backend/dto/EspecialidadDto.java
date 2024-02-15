package com.guardias.backend.dto;

import java.util.HashSet;
import java.util.Set;

import com.guardias.backend.entity.Asistencial;
import com.guardias.backend.entity.Profesion;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EspecialidadDto {
    @NotBlank
    private String nombre;
    private Profesion profesion;
    private boolean esPasiva;
    private Set<Asistencial> asistenciales = new HashSet<>();

}
