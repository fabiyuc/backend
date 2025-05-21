package com.guardias.backend.dto.cronogramaTentativo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidacionCronogramaResponseDto {

    private boolean coincideExactamente;
    private boolean existeDistribucionParcial;
    private boolean sinDistribucion;
}
