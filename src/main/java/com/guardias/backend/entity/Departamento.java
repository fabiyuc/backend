package com.guardias.backend.entity;

import java.util.List;

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

@Entity(name = "departamentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(50)")
    private String nombre;

    @Column(columnDefinition = "VARCHAR(10)")
    private String codigoPostal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provincia")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "departamentos" })
    Provincia provincia;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departamento", cascade = CascadeType.ALL)
    List<Localidad> localidades;

}
