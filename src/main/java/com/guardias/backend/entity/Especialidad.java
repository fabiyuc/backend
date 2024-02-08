package com.guardias.backend.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "especialidades")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(50)")
    private String nombre;
    private boolean esPasiva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesion")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "especialidades", "asistencial", "legajos" })
    private Profesion profesion;

    // @ManyToMany(mappedBy = "especialidades")
    // private Set<Asistencial> asistenciales;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "especialidades", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Asistencial> asistenciales = new HashSet<>();

}