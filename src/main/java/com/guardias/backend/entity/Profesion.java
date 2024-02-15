package com.guardias.backend.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "profesiones")
@Data
// @RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Profesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(25)")
    private String nombre;
    private Boolean asistencial;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profesion", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "profesion", "asistenciales", "legajos" })
    private Set<Especialidad> especialidades;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profesion", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "profesion", "legajos" })
    private Set<Legajo> legajos;

}
