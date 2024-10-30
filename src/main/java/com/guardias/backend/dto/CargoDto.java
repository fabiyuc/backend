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
    /*
     * List<Long> idLegajos;
     */

    List<Long> idAutoridades;

    /*
     * @NotNull
     * private AgrupacionEnum agrupacion;
     */
}

// genera todo el json
// {
// "nombre": "nombre",
// "descripcion": "descripcion",
// "nroresolucion": "nroresolucion",
// "nrodecreto": "nrodecreto",
// "fechaResolucion": "2021-09-01",
// "fechaInicio": "2021-09-01",
// "fechaFinal": "2021-09-01",
// "activo": true,
// "idLegajos": [1, 2, 3],
// "idAutoridades": [1, 2, 3]

// }