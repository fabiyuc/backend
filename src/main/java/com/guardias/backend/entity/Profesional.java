package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity(name = "profesionales")
@AllArgsConstructor
@NoArgsConstructor
public class Profesional extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricula_provincial", columnDefinition = "VARCHAR(10)")
    private String matriculaProvincial;

    @Column(name = "matricula_nacional", columnDefinition = "VARCHAR(10)")
    private String matriculaNacional;

}