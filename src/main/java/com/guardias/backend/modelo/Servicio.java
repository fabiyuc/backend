package com.guardias.backend.modelo;

import jakarta.persistence.*;

@Entity
public class Servicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String descripcion;

    public Servicio(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Servicio(String descripcion) {
        this.descripcion = descripcion;
    }

    public Servicio() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
