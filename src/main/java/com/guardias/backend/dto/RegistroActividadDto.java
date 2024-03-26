package com.guardias.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.guardias.backend.enums.TipoGuardiaEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroActividadDto {

    @NotBlank
    private String servicio;

    @NotBlank
    private LocalDate fechaIngreso;

    private LocalDate fechaEgreso;
    private boolean activo;

    @NotBlank
    private LocalTime horaIngreso;

    private LocalTime horaEgreso;

    @NotBlank
    private TipoGuardiaEnum tipoGuardia;

    private Long idAsistencial;
    private Long idEfector;

}
