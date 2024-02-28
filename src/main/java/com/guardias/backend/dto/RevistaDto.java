package com.guardias.backend.dto;

import java.util.Set;
import com.guardias.backend.entity.Adicional;
import com.guardias.backend.entity.CargaHoraria;
import com.guardias.backend.entity.Categoria;
import com.guardias.backend.entity.Legajo;
import com.guardias.backend.entity.TipoRevista;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevistaDto {

    @NotBlank
    private TipoRevista tipoRevista;

    @NotBlank
    private Categoria categoria;
    private boolean activo;

    @NotBlank
    private Adicional adicional;

    @NotBlank
    private CargaHoraria cargaHoraria;
    private Set<Legajo> legajos;

}
