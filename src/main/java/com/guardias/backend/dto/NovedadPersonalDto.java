package com.guardias.backend.dto;

import java.time.LocalDate;

import com.guardias.backend.entity.Person;
import com.guardias.backend.entity.TipoLicencia;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NovedadPersonalDto {

    @NotBlank
    private LocalDate fechaInicio;
    @NotBlank
    private LocalDate fechaFinal;
    private boolean puedeRealizarGuardia;
    private boolean cobraSueldo;
    private boolean necesitaReemplazo;
    private String descripcion;
    @NotBlank
    private Person persona;
    private Person reemplazante;
    @NotBlank
    private TipoLicencia tipoLicencia;
}
