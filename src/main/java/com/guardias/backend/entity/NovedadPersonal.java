package com.guardias.backend.entity;

import java.time.LocalDate;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "novedadesPersonales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovedadPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaFinal;
    private boolean puedeRealizarGuardia;
    private boolean cobraSueldo;
    private boolean necesitaReemplazo;
    @Column(nullable = false, columnDefinition = "BIT DEFAULT 1")
    private boolean activa; // Si la novedad es actual(1) o pasada(0)
    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_novedades")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "novedades" })
    private Person persona;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_suplente")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "suplentes" })
    private Person suplente;

    @OneToOne(mappedBy = "novedadPersonal")
    private Articulo articulo;

    @OneToOne(mappedBy = "novedadPersonal")
    private Inciso inciso;

}
