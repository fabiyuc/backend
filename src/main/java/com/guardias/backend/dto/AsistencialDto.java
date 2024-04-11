package com.guardias.backend.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AsistencialDto extends PersonDto {

    @NotBlank
    private List<Long> idLegajos;

    private List<Long> idRegistrosActividades;

    private List<Long> idTiposGuardias;
}
