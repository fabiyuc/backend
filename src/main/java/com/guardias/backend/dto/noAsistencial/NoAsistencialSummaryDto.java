package com.guardias.backend.dto.noAsistencial;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoAsistencialSummaryDto {

    private Long id;
    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
    @Min(value = 1000000)
    private int dni;
    @NotBlank
    private String cuil;
    @NotBlank
    private LocalDate fechaNacimiento;
    private String sexo;
    private String telefono;
    private String email;
    private String domicilio;

}
