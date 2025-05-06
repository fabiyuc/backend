package com.guardias.backend.dto.distribucionGuardia;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistribucionCheckDto {
    private Long idPersona;
    private Long idEfector;
    private LocalDate fecha;
   
}
