package com.guardias.backend.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipoUsuario")
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tipo_usuario;

    private String descripcion;

    public Long getId_tipo_usuario() {
        return id_tipo_usuario;
    }

    public void setId_tipo_usuario(Long id_tipo_usuario) {
        this.id_tipo_usuario = id_tipo_usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoUsuario(Long id_tipo_usuario, String descripcion) {
        this.id_tipo_usuario = id_tipo_usuario;
        this.descripcion = descripcion;
    }

    public TipoUsuario() {
    }

}
