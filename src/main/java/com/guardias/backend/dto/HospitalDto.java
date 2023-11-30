package com.guardias.backend.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HospitalDto extends EfectorDto {

    private boolean esCabecera;

    @Min(value = 1)
    private int nivelComplejidad;

}
