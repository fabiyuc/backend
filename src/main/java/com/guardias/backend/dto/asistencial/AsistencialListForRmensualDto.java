package com.guardias.backend.dto.asistencial;

import java.util.List;

import com.guardias.backend.dto.legajo.LegajoListDto;
import com.guardias.backend.dto.novedadPersonal.NovedadPersonalListDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistencialListForRmensualDto {

    private String apellido;
    private String nombre;
    private String cuil;
    private List<LegajoListDto> legajos;
    private List<NovedadPersonalListDto> novedadesPersonales;
}
