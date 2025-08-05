package com.guardias.backend.dto.revista;

import com.guardias.backend.dto.adicional.AdicionalListDto;
import com.guardias.backend.dto.categoria.CategoriaListDto;
import com.guardias.backend.dto.tipoRevista.TipoRevistaListDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevistaListDto {
    private TipoRevistaListDto tipoRevista;
    private CategoriaListDto categoria;
    private AdicionalListDto adicional;
}
