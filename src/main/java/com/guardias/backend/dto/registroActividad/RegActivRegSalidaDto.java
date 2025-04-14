package com.guardias.backend.dto.registroActividad;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegActivRegSalidaDto {
    
    private Long id;
    private LocalDate fechaIngreso;
    private String  horaIngreso;
    private Long idTipoGuardia;
    private Long idAsistencial;
    private Long idServicio;
    private Long idEfector;
    private Long idUsuarioIngreso;
}
