package com.guardias.backend.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FeriadoDto {
    @NotNull
    private LocalDate dia;
    @NotBlank
    private String motivo;
    @NotBlank
    private String tipoFeriado;
    private String descripcion;

    public FeriadoDto() {
    }

    public FeriadoDto(@NotBlank LocalDate dia, @NotBlank String motivo, @NotBlank String tipoFeriado,
            String descripcion) {
        this.dia = dia;
        this.motivo = motivo;
        this.tipoFeriado = tipoFeriado;
        this.descripcion = descripcion;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTipoFeriado() {
        return tipoFeriado;
    }

    public void setTipoFeriado(String tipoFeriado) {
        this.tipoFeriado = tipoFeriado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
