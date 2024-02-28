package com.guardias.backend.dto;

import java.util.List;
import com.guardias.backend.entity.Ministerio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MinisterioDto extends EfectorDto {

    Ministerio cabecera;
    private boolean activo;
    List<Ministerio> ministerios;

}
