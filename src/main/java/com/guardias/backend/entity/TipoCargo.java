package com.guardias.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity(name = "tiposcargos")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TipoCargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private boolean eshospitalario;

}