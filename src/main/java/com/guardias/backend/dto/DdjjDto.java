package com.guardias.backend.dto;

import java.math.BigDecimal;
import java.util.List;

import com.guardias.backend.enums.CondicionDdjjEnum;
import com.guardias.backend.enums.EstadoDdjjEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.QuincenaEnum;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DdjjDto {

    @NotBlank
    private MesesEnum mes;
    @Min(value = 1991)
    private int anio;
    @Min(value = 1)
    private boolean activo;
    @NotBlank
    private BigDecimal subtotal;
    @NotBlank
    private BigDecimal total;
    /* 9/6/25 idValorGmi no la usaremos por ahora */
    private Long idValorGmi;

    @NotBlank
    private Long idEfector;
    @NotBlank
    List<Long> idRegistrosMensuales;

    private Long idDirector;
    private Long idDirectorDPH;

    @NotBlank
    private EstadoDdjjEnum estadoDdjjDirector;
    private EstadoDdjjEnum estadoDdjjDirectorDPH;

    private Boolean enPosesionDirector;
    private Boolean enPosesionDirectorDPH;

    private String motivoDirector;
    private String motivoDirectorDPH;

    List<Long> idObservacionesDdjj;
    
    @NotBlank
    private Long idTipoGuardia;

    private List<Long> idCronogramasDefinitivos;

    private QuincenaEnum quincena;

    private CondicionDdjjEnum condicionDdjj;

}

