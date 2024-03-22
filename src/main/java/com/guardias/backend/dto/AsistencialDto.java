package com.guardias.backend.dto;

import java.util.List;

import com.guardias.backend.enums.TipoGuardiaEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AsistencialDto extends PersonDto {

    @NotBlank
    private List<Long> idLegajos;

    @NotBlank
    private TipoGuardiaEnum tipoGuardia;
    private List<Long> idEspecialidades;
    private List<Long> idRegistrosActividades;
}
