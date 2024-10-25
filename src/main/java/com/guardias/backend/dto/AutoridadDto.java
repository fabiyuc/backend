package com.guardias.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoridadDto {

    @NotBlank
    private String nombre;

    @NotNull
    private LocalDate fechaInicio;

    private LocalDate fechaFinal;

    private boolean esRegional;

    private boolean activo;

    @NotNull
    private Long idEfector;
    // private Efector efector;

    @NotNull
    private Long idPersona;
    // private Person persona;

    private Long idCargo;
}

// genera el json

// {
// "nombre": "nombre",
// "fechaInicio": "2021-09-01",
// "fechaFinal": "2021-09-01",
// "esRegional": true,
// "activo": true,
// "idEfector": 1,
// "idPersona": 1,
// "idCargo": 1
// }