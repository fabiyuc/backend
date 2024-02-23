package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.Set;

import com.guardias.backend.entity.DistribucionHoraria;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.NovedadPersonal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

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
    private Boolean estado;
    private boolean activo;

    private Set<NovedadPersonal> novedadesPersonales;

    private Set<NovedadPersonal> suplentes;

    private Set<DistribucionHoraria> distribucionesHorarias;

    private Set<AutoridadDto> autoridades;

    private Set<Legajo> legajos;

}
