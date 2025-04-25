package com.guardias.backend.entity;

import java.time.LocalDate;
import java.time.LocalTime;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "cronogramasTentativos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronogramaTentativo {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Temporal(TemporalType.DATE)
        private LocalDate fechaIngreso;

        @Temporal(TemporalType.DATE)
        private LocalDate fechaEgreso;

        @Temporal(TemporalType.TIME)
        private LocalTime horaIngreso;

        @Temporal(TemporalType.TIME)
        private LocalTime horaEgreso;

        @Column(columnDefinition = "BIT DEFAULT 1")
        private boolean activo;

        @Column(columnDefinition = "BIT DEFAULT 1")
        private boolean aceptado;

        @Column(columnDefinition = "BIT DEFAULT 1")
        private boolean autorizado;

        @ManyToOne(fetch = FetchType.LAZY, optional = true)
        @JoinColumn(name = "id_tipo_guardia")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "asistenciales", "activo",
                        "registrosActividades", "descripcion", "cronogramasTentativos", "legajos" })
        private TipoGuardia tipoGuardia;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_asistencial")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
                        "estado", "tipoGuardia", "descripcion", "tiposGuardias", "registrosActividades", "dni",
                        "fechaNacimiento",
                        "sexo", "telefono", "email", "domicilio", "esAsistencial", "activo", "suplentes", "autoridades",
                        "cronogramasTentativos", "legajos", "novedadesPersonales", "distribucionesHorarias",
                        "registrosMensuales", "usuarios", "habilitacionesGenerales", "habilitacionesGuardias",
                        "cronogramasDefinitivos" })
        private Asistencial asistencial;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_efector")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "autoridades", "domicilio", "telefono", "estado",
                        "activo", "observacion", "region", "localidad", "distribucionesHorarias", "legajosUdo",
                        "legajos",
                        "tipoCaps", "nivelComplejidad", "cabecera", "ministerios", "registrosActividades",
                        "registroMensual", "ddjjs",
                        "registrosPendientes", "servicios", "cronogramasTentativos", "habilitacionesGuardias",
                        "habilitacionesGenerales", "feriados", "cronogramasDefinitivos" })
        private Efector efector;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_servicio")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "efector", "cronogramasTentativos", "legajos" })
        private Servicio servicio;

        @Column(columnDefinition = "VARCHAR(50)")
        private String observacion;
}
