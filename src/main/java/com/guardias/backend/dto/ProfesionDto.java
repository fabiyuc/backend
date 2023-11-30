package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfesionDto {

    @NotBlank
    private String nombre;

    @NotBlank
    private Boolean esAsistencial;

}
