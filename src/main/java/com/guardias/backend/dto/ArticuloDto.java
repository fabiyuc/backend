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
public class ArticuloDto extends LeyDto {

    Articulo articulo;

    List<Articulo> subArticulos;

    List<Inciso> incisos;
}
