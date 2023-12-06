package com.guardias.backend.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tiposcargos")
@Data
// @RequiredArgsConstructor
@AllArgsConstructor

@NoArgsConstructor
public class TipoCargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(20)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;
    private boolean eshospitalario;

    @OneToMany(mappedBy = "tipoCargo")
    private Set<Cargo> cargos;
}
