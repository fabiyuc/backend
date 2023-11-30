package com.guardias.backend.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MinisterioDto extends EfectorDto {

    @Min(value = 1)
    private Long idCabecera;

}
