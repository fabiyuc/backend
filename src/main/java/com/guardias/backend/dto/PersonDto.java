package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.List;

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
    private Boolean esAsistencial;
    private boolean activo;

    private List<Long> idNovedadesPersonales;
    private List<Long> idSuplentes;
    private List<Long> idDistribucionesHorarias;
    private List<Long> idAutoridades;
    private List<Long> idLegajos;
    private List<Long> idRegistrosMensuales;

}
