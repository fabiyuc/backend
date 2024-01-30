package com.guardias.backend.entity;

import java.time.LocalDate;

import com.guardias.backend.enums.TipoNotificacion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "notificaciones")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(columnDefinition = "VARCHAR(30)")
    // private String tipo;

    @Column(columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING) // Cambiado a INTEGER para mapear el enum
    private TipoNotificacion tipo;

    @Column(columnDefinition = "VARCHAR(50)")
    private String categoria;

    // @JsonFormat(pattern = "dd/MM/yyyy")
    // @Column(columnDefinition = "DATE")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaNotificacion;

    @Column(columnDefinition = "VARCHAR(80)")
    private String detalle;

    @Column(columnDefinition = "VARCHAR(50)")
    private String url;

    @Column
    private boolean activo;

    // @JsonFormat(pattern = "dd/MM/yyyy")
    // @Column(columnDefinition = "DATE")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaBaja;
}
