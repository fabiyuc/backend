package com.guardias.backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoAsistencialDto {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String dni;

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
    private Boolean estado;

    @NotBlank
    private String descripcion;

}
