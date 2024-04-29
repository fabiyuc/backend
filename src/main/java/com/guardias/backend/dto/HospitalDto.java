package com.guardias.backend.dto;

import java.util.List;

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
    private boolean activo;

    @Min(value = 1)
    private int nivelComplejidad;

    List<Long> idCaps;

}
