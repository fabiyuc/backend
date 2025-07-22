package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.guardias.backend.enums.LocationEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private Boolean esAutoridad;
    @NotBlank
    private Boolean esRegional;
    private Boolean activo;
    private String url;
    private String matriculaNacional;
    @NotBlank
    private String matriculaProvincial;
    private Long idSuspencion;
    private String motivoBaja;

    private Long idRevista;

    private Long idUdo;
    @NotBlank
    private Long idPersona;
    @NotNull
    private List<Long> idEfectores;
    private List<Long> idEspecialidades;

    private Long idProfesion;

    private List<Long> idTipoGuardias;

    private Long idCargo;

    private Long idRegion;

    private String nroResolucion;

    private String nroDecreto;

    private LocalDate fechaResolucion;

    private LocalDate fechaBajaSistema;

    private LocationEnum tipoEfector;

    private LocationEnum tipoEfectorCargo;

    private LocationEnum tipoUdo;

}

// genera el json
// {
// "fechaInicio": "2021-09-01",
// "fechaFinal": "2021-09-01",
// "esAutoridad": true,
// "esRegional": true,
// "activo": true,
// "matriculaNacional": "matriculaNacional",
// "matriculaProvincial": "matriculaProvincial",
// "idSuspencion": 0,
// "motivoBaja": "motivoBaja",
// "idRevista": 0,
// "idUdo": 0,
// "idPersona": 0,
// "idEfectores": [0],
// "idEspecialidades": [0],
// "idProfesion": 0,
// "idTipoGuardias": [0],
// "idCargo": 0,
// "idRegion": 0,
// "nroResolucion": "nroResolucion",
// "nroDecreto": "nroDecreto",
// "fechaResolucion": "2021-09-01",
// "fechaBajaSistema": "2021-09-01",
// "tipoEfector": "CAPS",
// "tipoUdo": "CAPS"
// }
