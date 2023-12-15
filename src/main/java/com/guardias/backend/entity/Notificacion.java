package com.guardias.backend.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "notificaciones")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(30)")
    private String tipo;
    @Column(columnDefinition = "VARCHAR(10)")
    private Long posicion;
    @Column(columnDefinition = "VARCHAR(50)")
    private String categoria;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(columnDefinition = "DATE")
    private LocalDate fechanotificacion;

    @Column(columnDefinition = "VARCHAR(80)")
    private String detalle;
    @Column(columnDefinition = "VARCHAR(50)")
    private String url;

}
