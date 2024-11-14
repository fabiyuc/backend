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

    @NotBlank
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    @NotBlank
    private Boolean esAutoridad;
    private boolean activo;
    private String matriculaNacional;
    @NotBlank
    private String matriculaProvincial;
    private Long idSuspencion;
    private String motivoBaja;
    @NotBlank
    private Long idRevista;
    @NotBlank
    private Long idUdo;
    @NotBlank
    private Long idPersona;
    /*
     * @NotBlank
     * private Long idCargo;
     */
    @NotNull
    private List<Long> idEfectores;
    private List<Long> idEspecialidades;

    @NotBlank
    private Long idProfesion;

    private List<Long> idTipoGuardias;

}

// genera el json
// {
// "fechaInicio": "2021-09-01",
// "fechaFinal": "2021-09-01",
// "esAutoridad": true,
// "activo": true,
// "matriculaNacional": "123456",
// "matriculaProvincial": "123456",
// "idSuspencion": 1,
// "idRevista": 1,
// "idUdo": 1,
// "idPersona": 1,
// "idCargo": 1,
// "idEfectores": [1, 2],
// "idEspecialidades": [1, 2],
// "idProfesion": 1
// }