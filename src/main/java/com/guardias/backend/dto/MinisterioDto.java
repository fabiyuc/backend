package com.guardias.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class MinisterioDto extends EfectorDto {

    @Min(value = 1)
    private Long idCabecera;

    public MinisterioDto() {
    }

    public MinisterioDto(@NotBlank String nombre, @NotBlank String domicilio, String telefono, @NotBlank boolean estado,
            String observacion, Long idRegion, Long idLocalidad, @Min(1) Long idCabecera) {
        super(nombre, domicilio, telefono, estado, observacion, idRegion, idLocalidad);
        this.idCabecera = idCabecera;
    }

    public MinisterioDto(@Min(1) Long idCabecera) {
        this.idCabecera = idCabecera;
    }

    public Long getIdCabecera() {
        return idCabecera;
    }

    public void setIdCabecera(Long idCabecera) {
        this.idCabecera = idCabecera;
    }

}
