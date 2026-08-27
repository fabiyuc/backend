package com.guardias.backend.dto.cronogramaTentativo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalHorasDto {
    private LocalDate fecha;
    private Long idAsistencial;
    private String nombreAsistencial;
    private String apellidoAsistencial;
    private Long idServicio;
    private String nombreServicio;
    private Double totalHoras;
}