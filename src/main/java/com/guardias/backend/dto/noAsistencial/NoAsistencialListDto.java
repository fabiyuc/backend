package com.guardias.backend.dto.noAsistencial;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoAsistencialListDto {
    private Long id;
    private String nombre;
    private String apellido;
    private int dni;
    private String cuil;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String telefono;
    private String email;
    private String domicilio;
}