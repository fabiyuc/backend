package com.guardias.backend.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuspencionDto {

    @NotBlank
    private String descripcion;

    @NotBlank
    private Date fechaInicio;

    @NotBlank
    private Date fechaFin;

}
