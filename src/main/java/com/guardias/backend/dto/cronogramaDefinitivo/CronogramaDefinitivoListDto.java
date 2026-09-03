package com.guardias.backend.dto.cronogramaDefinitivo;

import java.util.List;

import com.guardias.backend.dto.registroActividad.RegistroActividadListDto;
import com.guardias.backend.enums.MesesEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronogramaDefinitivoListDto {

    private Long id;
    private MesesEnum mes;
    private int anio;
    private List<RegistroActividadListDto> registrosActividades;
    //private List<DdjjListDto> ddjjs;
}
