package com.guardias.backend.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
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
    private String nombre;
    private Boolean asistencial;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profesion", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "profesion" })
    private Set<Legajo> legajos;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profesion", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "profesion" })
    private Set<Especialidad> especialidades;

}
