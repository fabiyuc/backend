package com.guardias.backend.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "autoridades")
@Data
@AllArgsConstructor
// @RequiredArgsConstructor
@NoArgsConstructor
public class Autoridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(25)")
    private String nombre;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaFinal;
    private boolean esActual;
    private boolean esRegional;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "autoridad_efector", joinColumns = @JoinColumn(name = "id_autoridad"), inverseJoinColumns = @JoinColumn(name = "id_efector"))
    private Set<Efector> efectores;

}
