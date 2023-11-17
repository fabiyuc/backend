package com.guardias.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "CargasHorarias")
@Data
public class CargaHoraria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int cantidad;

    public CargaHoraria() {
    }

    public CargaHoraria(int cantidad) {
        this.cantidad = cantidad;
    }

}
