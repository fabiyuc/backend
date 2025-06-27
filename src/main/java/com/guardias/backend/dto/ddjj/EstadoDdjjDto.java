package com.guardias.backend.dto.ddjj;

import com.guardias.backend.enums.EstadoDdjjEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoDdjjDto {

    private Long idDdjj;

    private Long idDirector;
    private Long idDirectorDPH;

    private EstadoDdjjEnum estadoDdjjDirector;
    private EstadoDdjjEnum estadoDdjjDirectorDPH;

    private Boolean enPosesionDirector;
    private Boolean enPosesionDirectorDPH;
    
    private String motivoDirector;
    private String motivoDirectorDPH;
}
