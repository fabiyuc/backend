package com.guardias.backend.dto;

import java.time.LocalDate;

import com.guardias.backend.entity.Person;
import com.guardias.backend.entity.TipoLicencia;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NovedadPersonalDto {

    @NotNull
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private boolean puedeRealizarGuardia;
    private boolean cobraSueldo;
    private boolean necesitaReemplazo;
    private String descripcion;

    @NotEmpty
    private Person persona;

    private Person reemplazante;
    @NotEmpty
    private TipoLicencia tipoLicencia;
}
