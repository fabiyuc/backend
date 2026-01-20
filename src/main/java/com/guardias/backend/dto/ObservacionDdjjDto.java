package com.guardias.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObservacionDdjjDto {
    
    private Boolean activo;

    @NotBlank
    private String motivo;

    @NotBlank
    private Boolean tipoDph;

    @NotNull
    private Long idUsuario;

    @NotNull
    private Long idDdjj;

    @NotBlank
    private LocalDate fechaCreacion;

    @NotBlank
    private LocalTime horaCreacion;

    private String documentoRespaldo;

}
