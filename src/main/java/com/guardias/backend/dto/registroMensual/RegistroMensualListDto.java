package com.guardias.backend.dto.registroMensual;

import java.util.List;

import com.guardias.backend.dto.asistencial.AsistencialListForRmensualDto;
import com.guardias.backend.dto.registroActividad.RegActivListDto;
import com.guardias.backend.dto.sumaHoras.SumaHorasListDto;
import com.guardias.backend.enums.MesesEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroMensualListDto {
    
    private Long id;
    private MesesEnum mes;
    private int anio;
    private AsistencialListForRmensualDto asistencial;
    private List<RegActivListDto> registroActividad;
    private SumaHorasListDto totalHoras;
    private Long idDdjj;
}
