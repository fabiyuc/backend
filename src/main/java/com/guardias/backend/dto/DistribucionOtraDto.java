//dto/DistribucionOtraDto
package com.guardias.backend.dto;

import com.guardias.backend.enums.TipoDistribucionOtraEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DistribucionOtraDto extends DistribucionHorariaDto {
    private String descripcion;
    private String lugar;
    private TipoDistribucionOtraEnum tipo;
}
