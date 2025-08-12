package com.guardias.backend.dto.ddjj;

import java.util.List;

import com.guardias.backend.dto.registroMensual.RegistroMensualListDto;
import com.guardias.backend.enums.EstadoDdjjEnum;
import com.guardias.backend.enums.MesesEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DdjjListDto {
    private Long id;
    private MesesEnum mes;
    private int anio;
    private List<RegistroMensualListDto> registrosMensuales;
    private Long idDirector;
    private Long idDirectorDPH;
    private EstadoDdjjEnum estadoDdjjDirector;
    private EstadoDdjjEnum estadoDdjjDirectorDPH;
    private Boolean enPosesionDirector;
    private Boolean enPosesionDirectorDPH;
    private String motivoDirector;
    private String motivoDirectorDPH;
    private Long idTipoGuardia;
    
}
