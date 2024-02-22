package com.guardias.backend.dto;

//import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

import com.guardias.backend.entity.Legajo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuspencionDto {

    @NotBlank
    private String descripcion;

    @NotBlank
    private LocalDate fechaInicio;
    private boolean activo;

    @NotBlank
    private LocalDate fechaFin;

    @NotBlank
    private Set<Legajo> legajos;

}
