package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.Set;

import com.guardias.backend.entity.NovedadPersonal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    private String nombre;
    private String apellido;
    private String dni;
    private String cuil;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String numCelular;
    private String email;
    private String domicilio;
    private Boolean estado;

    private Set<NovedadPersonal> novedadesPersonales;

    private Set<NovedadPersonal> suplente;

}
