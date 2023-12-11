package com.guardias.backend.entity;

import java.util.List;

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

@Entity(name = "paises")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(25)")
    private String nombre;

    @Column(columnDefinition = "VARCHAR(25)")
    private String nacionalidad;

    @Column(columnDefinition = "VARCHAR(5)")
    private String codigo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pais", cascade = CascadeType.ALL)
    List<Provincia> provincias;

}
