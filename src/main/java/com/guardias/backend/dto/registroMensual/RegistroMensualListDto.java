package com.guardias.backend.dto.registroMensual;

import java.util.List;

import com.guardias.backend.dto.asistencial.AsistencialListForRmensualDto;
import com.guardias.backend.dto.factura.FacturaDetailDto;
import com.guardias.backend.dto.registroActividad.RegActivListDto;
import com.guardias.backend.dto.sumaHoras.SumaHorasListDto;
import com.guardias.backend.enums.EstadoFacturacionEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.QuincenaEnum;

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
    private List<Long> idDdjjs;
    private QuincenaEnum quincena;

    private EstadoFacturacionEnum estadoFacturacion;
    private List<FacturaDetailDto> facturas;

}
