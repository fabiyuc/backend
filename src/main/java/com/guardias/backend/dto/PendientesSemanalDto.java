package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.guardias.backend.enums.MesesEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendientesSemanalDto {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private MesesEnum mes;
    private int anio;
    private Long idEfector;
    private List<Long> idRegistrosPendientes;
}
