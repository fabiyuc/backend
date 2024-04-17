package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegajoDto {

    @NotNull
    private LocalDate fechaInicio;

    private LocalDate fechaFinal;

    @NotBlank
    private Boolean actual;

    @NotBlank
    private Boolean legal;
    private boolean activo;

    private String matriculaNacional;
    private String matriculaProvincial;

    @NotBlank
    private Long idProfesion;

    private Long idSuspencion;

    @NotBlank
    private Long idRevista;

    private Long idCargo;
    @NotBlank
    private Long idPersona;

    @NotBlank
    private Long idUdo;

    private List<Long> idEfectores;
}
