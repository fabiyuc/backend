package com.guardias.backend.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ArticuloDto extends LeyDto {

    Long idArticulo;
    Set<Long> idSubArticulos;
    Set<Long> idIncisos;
}
