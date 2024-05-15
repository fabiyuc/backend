package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegajoDto {

    @NotBlank
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
    private Long idSuspencion;
    @NotBlank
    private Long idRevista;
    @NotBlank
    private Long idUdo;
    @NotBlank
    private Long idPersona;
    private Long idCargo;
    private List<Long> idEfectores = new ArrayList<>();
    private List<Long> IdEspecialidades = new ArrayList<>();

}
