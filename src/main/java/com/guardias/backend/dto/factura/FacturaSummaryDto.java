package com.guardias.backend.dto.factura;

import com.guardias.backend.dto.asistencial.AsistencialDetailDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaSummaryDto {
    private Long id;
    private AsistencialDetailDto asistencial;
}
