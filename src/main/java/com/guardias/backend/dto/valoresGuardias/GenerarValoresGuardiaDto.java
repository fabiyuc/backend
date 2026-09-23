package com.guardias.backend.dto.valoresGuardias;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerarValoresGuardiaDto {
    private LocalDate fecha;
}
