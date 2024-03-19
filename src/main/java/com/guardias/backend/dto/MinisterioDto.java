package com.guardias.backend.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MinisterioDto extends EfectorDto {

    Long idCabecera;
    Set<Long> idMinisterios;

}
