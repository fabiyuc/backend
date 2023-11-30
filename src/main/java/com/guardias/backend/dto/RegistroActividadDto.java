package com.guardias.backend.dto;

import java.sql.Date;

import com.guardias.backend.entity.TipoGuardia;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistroActividadDto {

    @NotBlank
    private String establecimiento;

    @NotBlank
    private String servicio;

    @NotBlank
    private Date fechaIngreso;

    private Date fechaEgreso;

    @NotBlank
    private String horaIngreso;

    private String horaEgreso;

    @NotBlank
    private TipoGuardia tipoGuardia;

}
