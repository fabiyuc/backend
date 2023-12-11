package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne; // Cambiada la importación
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tiposcargos")
@Data
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

    @OneToOne(mappedBy = "tipoCargo")
    private Cargo cargo;
}
