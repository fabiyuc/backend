package com.guardias.backend.dto;

import java.util.List;

import com.guardias.backend.enums.LocationEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabilitacionesGuardiasDto {

    private Boolean activo;
    @NotBlank
    private Long idAsistencial;
    @NotBlank
    private List<Long> idEfectores;

    private LocationEnum tipoEfector;

}
