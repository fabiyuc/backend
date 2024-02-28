package com.guardias.backend.entity;

import java.time.LocalDate;
import java.util.Set;
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

@Entity(name = "suspenciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Suspencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "suspencion", cascade = CascadeType.ALL)
    private Set<Legajo> legajos;
}
