package com.guardias.backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class HospitalDto extends EfectorDto {

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean esCabecera;

    @Min(value = 1)
    @Column(columnDefinition = "INT DEFAULT 1")
    private int nivelComplejidad;

    public HospitalDto(boolean esCabecera, @Min(1) int nivelComplejidad) {
        this.esCabecera = esCabecera;
        this.nivelComplejidad = nivelComplejidad;
    }

    public HospitalDto(@NotBlank String nombre, @NotBlank String domicilio, String telefono, @NotBlank boolean estado,
            String observacion, Long idRegion, Long idLocalidad, boolean esCabecera, @Min(1) int nivelComplejidad) {
        super(nombre, domicilio, telefono, estado, observacion, idRegion, idLocalidad);
        this.esCabecera = esCabecera;
        this.nivelComplejidad = nivelComplejidad;
    }

    public boolean isEsCabecera() {
        return esCabecera;
    }

    public void setEsCabecera(boolean esCabecera) {
        this.esCabecera = esCabecera;
    }

    public int getNivelComplejidad() {
        return nivelComplejidad;
    }

    public void setNivelComplejidad(int nivelComplejidad) {
        this.nivelComplejidad = nivelComplejidad;
    }

}
