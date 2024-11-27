package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
    private boolean activo;
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

    private String nroresolucion; 

    private String nrodecreto; 

    private LocalDate fechaResolucion; 
    
    private LocalDate fechaBajaSistema;

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