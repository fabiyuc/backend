package com.guardias.backend.dto;

import java.util.Set;

import com.guardias.backend.entity.Especialidad;
import com.guardias.backend.entity.Legajo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesionDto {

    @NotBlank
    private String nombre;

    @NotBlank
    private Boolean asistencial;

    @NotBlank
    private Set<Legajo> legajos;

    private Set<Especialidad> especialidades;

}
