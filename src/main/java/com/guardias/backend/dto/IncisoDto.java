package com.guardias.backend.dto;

import java.util.Set;

import com.guardias.backend.entity.Articulo;
import com.guardias.backend.entity.Inciso;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IncisoDto extends LeyDto {

    private Inciso inciso;
    private Set<Long> idSubIncisos;
    private Articulo articulo;
}
