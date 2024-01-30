package com.guardias.backend.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    private Boolean puedeRealizarGuardia;
    private Boolean cobraSueldo;
    private Boolean necesitaReemplazo;

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 1")
    private Boolean activa; // Si la novedad es actual(1) o pasada(0)

    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_persona")
    private Person persona;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_suplente")
    private Person suplente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_licencia")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "novedadesPersonales" })
    private TipoLicencia tipoLicencia;


    /* @OneToOne(mappedBy = "novedadPersonal")
    private TipoLicencia tipoLicencia; */

}
