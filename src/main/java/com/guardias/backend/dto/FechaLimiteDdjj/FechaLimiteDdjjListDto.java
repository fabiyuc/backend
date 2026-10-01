package com.guardias.backend.dto.FechaLimiteDdjj;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.guardias.backend.enums.EstadoFechaLimiteEnum;
import com.guardias.backend.enums.MesesEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FechaLimiteDdjjListDto {
    private Long id;
    private MesesEnum mes;
    private int anio;
    private Long idTipoGuardia;
    private String tipoGuardia;
    private LocalDate fechaLimite;
    private String motivo;
    private EstadoFechaLimiteEnum estado;
    private boolean activo;

    private Long idUsuarioCreacion;
    private String usuarioCreacion;
    private LocalDateTime fechaHoraCreacion;

    private Long idUsuarioBaja;
    private String usuarioBaja;
    private LocalDateTime fechaHoraBaja;
    private String motivoBaja;
}
