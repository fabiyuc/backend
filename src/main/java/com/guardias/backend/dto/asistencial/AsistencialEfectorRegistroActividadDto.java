package com.guardias.backend.dto.asistencial;

import java.time.LocalDate;
import java.util.List;

import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.RegistroActividad;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistencialEfectorRegistroActividadDto {
    private Long id;
    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
    @Min(value = 1000000)
    private int dni;
    @NotBlank
    private String cuil;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String telefono;
    private String email;
    private String domicilio;
    private Boolean esAsistencial;
    private boolean activo;
    private List<Legajo> idLegajos;
    private List<RegistroActividad> idRegistrosActividades;
    private List<String> nombresTiposGuardias;

}
