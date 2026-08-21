package com.guardias.backend.dto.asignacionHorasEfector;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumenAsignacionDto {
    
    private Integer anio;
    private Integer mes;

    private BigDecimal horasTotalesLegajo;   // cargaHoraria.cantidad del legajo
    private BigDecimal horasAsignadas;       // suma de lo asignado ESE mes, entre todos los efectores activos
    private BigDecimal horasSinAsignar;      // horasTotalesLegajo - horasAsignadas
    private boolean completo;                // true si horasSinAsignar == 0

    private List<DetalleAsignacionEfectorDto> detalle;
}
