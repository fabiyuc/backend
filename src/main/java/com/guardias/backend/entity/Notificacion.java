package com.guardias.backend.entity;

import java.time.LocalDate;
import java.util.Set;

import com.guardias.backend.enums.TipoNotificacionEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity(name = "notificaciones")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private TipoNotificacionEnum tipo;

    @Column(columnDefinition = "VARCHAR(50)")
    private String categoria;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaNotificacion;

    @Column(columnDefinition = "VARCHAR(80)")
    private String detalle;

    @Column(columnDefinition = "VARCHAR(50)")
    private String url;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaBaja;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "notificacion_efector", joinColumns = @JoinColumn(name = "notificacion_id"), inverseJoinColumns = @JoinColumn(name = "efector_id"))
    private Set<Efector> efectores;
}
