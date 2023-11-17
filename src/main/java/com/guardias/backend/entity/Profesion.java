package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity(name = "profesiones")
@Data
@RequiredArgsConstructor
public class Profesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(25)")
    private String nombre;
    private Boolean esAsistencial;

    public Profesion(String nombre, Boolean esAsistencial) {
        this.nombre = nombre;
        this.esAsistencial = esAsistencial;
    }

}
