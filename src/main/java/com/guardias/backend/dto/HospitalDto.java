package com.guardias.backend.dto;

import java.util.Set;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HospitalDto extends EfectorDto {

    private boolean esCabecera;
    private boolean admitePasiva;

    @Min(value = 1)
    private int nivelComplejidad;

    private Set<Long> idCaps;

}
