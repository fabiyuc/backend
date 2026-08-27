package com.guardias.backend.dto.cronogramaTentativo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalHorasResponseDto {
    private List<TotalHorasDto> detalles;
    private Double totalGeneral;
}