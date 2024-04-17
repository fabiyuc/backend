package com.guardias.backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ArticuloDto extends LeyDto {

    private Long idArticulo;
    private List<Long> idSubArticulos;
    private List<Long> idIncisos;
    private List<Long> idNovedadesPersonales;
}
