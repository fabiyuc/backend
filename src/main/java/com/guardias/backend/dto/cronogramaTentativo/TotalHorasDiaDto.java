package com.guardias.backend.dto.cronogramaTentativo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalHorasDiaDto {

    private LocalDate fecha;
    private Double totalHoras;
}