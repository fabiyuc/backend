package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.guardias.backend.enums.AgrupacionEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CargoDto {

    @NotBlank
    private String nombre;
    @NotBlank
    private String descripcion;
    @NotBlank
    private String nroresolucion;
    @NotBlank
    private String nrodecreto;
    @NotNull
    private LocalDate fechaResolucion;
    @NotNull
    private LocalDate fechaInicio;
    @NotNull
    private LocalDate fechaFinal;

    // private Boolean activo = true;

    private boolean activo;

    // private Legajo legajo;

    List<Long> idLegajos;

    @NotNull
    private AgrupacionEnum agrupacion;
}