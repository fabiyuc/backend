package com.guardias.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class CargaHorariaDto {
    
    @NotBlank
    private Long id;

    @NotBlank
    private int cantidad;

    public CargaHorariaDto() {
    }

    public CargaHorariaDto(@NotBlank Long id, @NotBlank int cantidad) {
        this.id = id;
        this.cantidad = cantidad;
    }

    public CargaHorariaDto(@NotBlank int cantidad) {
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    
}
