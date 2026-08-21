package com.guardias.backend.entity;

import java.math.BigDecimal;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "asignacionesHorasEfector")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsignacionHorasEfector {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    private BigDecimal horasAsignadas;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaFinalizacion; // null = vigente indefinidamente

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_legajo")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "especialidades", "tipoGuardias",
            "efectores", "revista", "udo", "persona", "cargo", "region", "profesion", "suspencion" })
    private Legajo legajo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_efector")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "autoridades", "domicilio", "telefono",
            "estado", "activo", "observacion", "region", "localidad", "distribucionesHorarias", "legajosUdo",
            "legajos", "notificaciones", "servicios", "registrosActividades", "registroMensual", "ddjjs",
            "registrosPendientes", "habilitacionesGuardias", "habilitacionesGenerales", "cronogramasTentativos",
            "feriados", "cronogramasDefinitivos" })
    private Efector efector;
}
