package com.guardias.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaDto {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @Min(value = 0)
    private int dni;

    @NotBlank
    private String cuil;

    private LocalDate fechaNac;

    private String sexo;

    private Boolean estado;

    private String domicilio;

    private String telefono;

    private String email;
}
