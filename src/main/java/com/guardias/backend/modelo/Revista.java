package com.guardias.backend.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "revista")
public class Revista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRevista;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "adicional")
    private String adicional;

    @Column(name = "idCargaHoraria")
    private Long idCargaHoraria;

    public Revista() {
    }

    public Revista(Long idRevista, String tipo, String categoria, String adicional, Long idCargaHoraria) {
        this.idRevista = idRevista;
        this.tipo = tipo;
        this.categoria = categoria;
        this.adicional = adicional;
        this.idCargaHoraria = idCargaHoraria;
    }

    public Long getIdRevista() {
        return idRevista;
    }

    public void setIdRevista(Long idRevista) {
        this.idRevista = idRevista;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getAdicional() {
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }

    public Long getIdCargaHoraria() {
        return idCargaHoraria;
    }

    public void setIdCargaHoraria(Long idCargaHoraria) {
        this.idCargaHoraria = idCargaHoraria;
    }

}
