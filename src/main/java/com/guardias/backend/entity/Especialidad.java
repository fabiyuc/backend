package com.guardias.backend.entity;

import java.util.List;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "especialidad", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Asistencial> asistenciales;
}