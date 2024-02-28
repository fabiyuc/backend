package com.guardias.backend.dto;

import com.guardias.backend.entity.Hospital;
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

    Hospital cabecera;

    @Min(value = 1)
    int areaProgramatica;

    @NotBlank
    private TipoCaps tipoCaps;

}
