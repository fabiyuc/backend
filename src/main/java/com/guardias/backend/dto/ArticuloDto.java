package com.guardias.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ArticuloDto extends LeyDto {

    Long idArticulo;
    List<Long> idSubArticulos;
    List<Long> idIncisos;
}
