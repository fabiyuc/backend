package com.guardias.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AsistencialDto {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private int dni;

    @NotBlank
    private String cuil;

    @NotBlank
    private LocalDate fechaNacimiento;

    @NotBlank
    private String sexo;

    @NotBlank
    private String numCelular;

    @NotBlank
    private String email;

    @NotBlank
    private String domicilio;

    @NotBlank
    private boolean estado;

}
