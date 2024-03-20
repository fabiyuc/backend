package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.Set;

import com.guardias.backend.entity.Legajo;
import com.guardias.backend.enums.AgrupacionEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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
    private LocalDate fecharesolucion;
    @NotNull
    private LocalDate fechainicio;
    @NotNull
    private LocalDate fechafinal;

    // private Boolean activo = true;

    private boolean activo;

    // private Legajo legajo;

    private Set<Legajo> legajos;

    @NotNull
    private AgrupacionEnum agrupacion;
}