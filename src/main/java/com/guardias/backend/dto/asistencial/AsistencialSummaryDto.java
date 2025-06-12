package com.guardias.backend.dto.asistencial;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistencialSummaryDto {

    private Long id;
    private String nombre;
    private String apellido;
    private String cuil;
    private String profesion;
    private List<String> nombresTiposGuardias;
    private Long idEfector;
    

}
