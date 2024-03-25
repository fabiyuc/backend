package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.guardias.backend.entity.Efector;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegajoDto {

    Long id;
    @NotBlank
    private LocalDate fechaInicio;

    private LocalDate fechaFinal;

    @NotBlank
    private Long idPersona;

    @NotBlank
    private Long idUdo;

    @NotBlank
    private Boolean actual;

    @NotBlank
    private Boolean legal;

    private String matriculaNacional;
    private String matriculaProvincial;

    @NotBlank
    private Long idProfesion;

    private Long idSuspencion;

    @NotBlank
    private Long idRevista;

    private String tipoGuardia;
    private String descripcion;
    private Long idCargo;
    private boolean activo;

    private List<Efector> efectores;
}
