package com.guardias.backend.dto;

import java.time.LocalDate;

import com.guardias.backend.entity.Articulo;
import com.guardias.backend.entity.Inciso;
import com.guardias.backend.entity.Person;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovedadPersonalDto {

    @NotNull
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private Boolean puedeRealizarGuardia;
    private Boolean cobraSueldo;
    private Boolean necesitaReemplazo;
    private Boolean activa;
    private String descripcion;
    private Long idExtensionLicencia;

    @NotEmpty
    private Person persona;
    private Person reemplazante;
    private Articulo articulo;
    private Inciso inciso;
}
