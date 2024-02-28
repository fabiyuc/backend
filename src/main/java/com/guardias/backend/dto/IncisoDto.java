package com.guardias.backend.dto;

import java.util.List;
import com.guardias.backend.entity.Articulo;
import com.guardias.backend.entity.Inciso;
import com.guardias.backend.entity.NovedadPersonal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IncisoDto extends LeyDto {

    Inciso inciso;
    List<Inciso> subIncisos;
    Articulo articulo;
    NovedadPersonal novedadPersonal;
}
