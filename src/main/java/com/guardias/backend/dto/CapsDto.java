package com.guardias.backend.dto;

import com.guardias.backend.enums.TipoCaps;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CapsDto extends EfectorDto {

    private Long idCabecera;

    @Min(value = 1)
    private int areaProgramatica;

    @NotBlank
    private TipoCaps tipoCaps;

}
