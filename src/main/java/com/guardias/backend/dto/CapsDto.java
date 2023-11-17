package com.guardias.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CapsDto extends EfectorDto {

    @Min(value = 1)
    private int idUdo;

    @NotBlank
    private String tipoCaps;

    public CapsDto() {
    }

    public CapsDto(@Min(1) int idUdo, @NotBlank String tipoCaps) {
        this.idUdo = idUdo;
        this.tipoCaps = tipoCaps;
    }

    public CapsDto(@NotBlank String nombre, @NotBlank String domicilio, String telefono, @NotBlank boolean estado,
            String observacion, Long idRegion, Long idLocalidad, @Min(1) int idUdo, @NotBlank String tipoCaps) {
        super(nombre, domicilio, telefono, estado, observacion, idRegion, idLocalidad);
        this.idUdo = idUdo;
        this.tipoCaps = tipoCaps;
    }

    public int getIdUdo() {
        return idUdo;
    }

    public void setIdUdo(int idUdo) {
        this.idUdo = idUdo;
    }

    public String getTipoCaps() {
        return tipoCaps;
    }

    public void setTipoCaps(String tipoCaps) {
        this.tipoCaps = tipoCaps;
    }

}
