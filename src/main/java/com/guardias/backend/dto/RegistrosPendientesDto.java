package com.guardias.backend.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrosPendientesDto {

    private LocalDate fecha;
    private boolean activo;
    private Long idEfector;
    private List<Long> idRegistrosActividades = new ArrayList<>();
}
