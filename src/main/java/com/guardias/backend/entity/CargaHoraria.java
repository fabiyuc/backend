package com.guardias.backend.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "cargasHorarias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CargaHoraria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;
    @Column(columnDefinition = "VARCHAR(50)")
    private String descripcion; // si lleva algun adicional o algo a tener en cuenta
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    /*
     * @OneToMany(fetch = FetchType.LAZY, mappedBy = "cargaHoraria", cascade =
     * CascadeType.ALL)
     * private Set<Revista> revistas;
     */

    @OneToMany(mappedBy = "cargaHoraria", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "cargaHoraria" })
    private Set<Revista> revistas = new HashSet<>();
}
