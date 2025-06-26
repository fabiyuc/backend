package com.guardias.backend.dto.registroActividad;

import java.time.LocalDate;

import com.guardias.backend.enums.TipoGuardiaEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegActivNombresDto {

    private Long id;
    private LocalDate fechaIngreso;
    private String horaIngreso;
    private TipoGuardiaEnum tipoGuardia;
    private String asistencial;
    private String servicio;
    private Long idEfector;
    private Long idUsuarioIngreso;

}
