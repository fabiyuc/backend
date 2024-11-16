package com.guardias.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoridadDto {

    private boolean activo;

    @NotNull
    private Long idPersona;
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