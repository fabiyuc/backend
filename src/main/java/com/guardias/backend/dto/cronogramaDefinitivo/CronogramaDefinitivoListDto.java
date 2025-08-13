package com.guardias.backend.dto.cronogramaDefinitivo;

import java.util.List;

import com.guardias.backend.dto.ddjj.DdjjListDto;
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
    private List<DdjjListDto> ddjjs;
}
