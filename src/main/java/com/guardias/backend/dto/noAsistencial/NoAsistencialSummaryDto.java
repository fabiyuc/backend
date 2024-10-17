package com.guardias.backend.dto.noAsistencial;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoAsistencialSummaryDto {

    private Long id;
    private String nombre;
    private String apellido;

}