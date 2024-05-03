package com.guardias.backend.dto;

import java.util.List;

import com.guardias.backend.enums.MesesEnum;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroMensualDto {

    @NotBlank
    private MesesEnum mes;
    @Min(value = 1991)
    private int anio;
    @Min(value = 1)
    private Long idAsistencial;

    private Long idEfector;

    private List<Long> idRegistroActividad;
    private Long idDdjj;
    private Long idSumaHoras;
    private boolean activo;

}
