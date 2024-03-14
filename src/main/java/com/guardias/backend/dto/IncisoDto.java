package com.guardias.backend.dto;

import java.util.List;

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
    private List<Inciso> subIncisos;
    private Articulo articulo;
    private Long idNovedadPersonal;
}
