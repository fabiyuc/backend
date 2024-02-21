package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.guardias.backend.entity.Cargo;
import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Person;
import com.guardias.backend.entity.Profesion;
import com.guardias.backend.entity.Revista;
import com.guardias.backend.entity.Suspencion;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegajoDto {

    Long id;
    @NotBlank
    private LocalDate fechaInicio;

    private LocalDate fechaFinal;

    @NotBlank
    private Person persona;

    @NotBlank
    private Efector udo;

    @NotBlank
    private Boolean actual;

    @NotBlank
    private Boolean legal;

    private String matriculaNacional;

    private String matriculaProvincial;

    @NotBlank
    private Profesion profesion;

    private Suspencion suspencion;

    @NotBlank
    private Revista revista;

    private String tipoGuardia;
    private String descripcion;

    private Cargo cargo;

    private Set<Efector> efectores = new HashSet<>();
}
