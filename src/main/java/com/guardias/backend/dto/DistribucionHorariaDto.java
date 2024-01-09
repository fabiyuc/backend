package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.Person;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionHorariaDto {

    private String tipo;
    @NotNull
    private LocalDate fecha;
    @NotNull
    private LocalTime horaIngreso;
    @NotNull
    private BigDecimal cantidadHoras;
    @NotNull
    private Efector efector;
    @NotNull
    private Person persona;
}
