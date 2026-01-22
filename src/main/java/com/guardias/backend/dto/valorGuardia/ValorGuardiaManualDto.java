package com.guardias.backend.dto.valorGuardia;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.guardias.backend.enums.TipoGuardiaEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValorGuardiaManualDto {
    
    private TipoGuardiaEnum tipoGuardia; 
    private int nivelComplejidad;        
    private BigDecimal totalLav;         
    private BigDecimal totalSdf;        
    private LocalDate fechaInicio;    
    
    // Lista de IDs de hospitales a los que aplica este valor.
    // Si la lista está vacía o es null, se asume que es "Resto de nivel"
    private List<Long> idsHospitales;
}
