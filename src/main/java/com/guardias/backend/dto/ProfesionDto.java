package com.guardias.backend.dto;

import java.util.List;

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
    private Boolean activo;

    @NotBlank
    private List<Long> idLegajos;

    private List<Long> idEspecialidades;

}
