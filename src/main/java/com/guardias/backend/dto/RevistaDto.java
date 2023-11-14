package com.guardias.backend.dto;

import com.guardias.backend.entity.Adicional;
import com.guardias.backend.entity.CargaHoraria;
import com.guardias.backend.entity.Categoria;
import com.guardias.backend.entity.TipoRevista;
import jakarta.validation.constraints.NotBlank;

public class RevistaDto {
    
    @NotBlank
    private Long id;

    @NotBlank
    private TipoRevista tipoRevista;

    @NotBlank
    private Categoria categoria;

    @NotBlank
    private Adicional adicional;

    @NotBlank
    private CargaHoraria cargaHoraria;

    public RevistaDto() {
    }

    public RevistaDto(@NotBlank TipoRevista tipoRevista, @NotBlank Categoria categoria, @NotBlank Adicional adicional,
            @NotBlank CargaHoraria cargaHoraria) {
        this.tipoRevista = tipoRevista;
        this.categoria = categoria;
        this.adicional = adicional;
        this.cargaHoraria = cargaHoraria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoRevista getTipoRevista() {
        return tipoRevista;
    }

    public void setTipoRevista(TipoRevista tipoRevista) {
        this.tipoRevista = tipoRevista;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Adicional getAdicional() {
        return adicional;
    }

    public void setAdicional(Adicional adicional) {
        this.adicional = adicional;
    }

    public CargaHoraria getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(CargaHoraria cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    
}
