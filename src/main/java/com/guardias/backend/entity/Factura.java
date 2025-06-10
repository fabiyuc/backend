package com.guardias.backend.entity;

import java.math.BigDecimal;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "facturas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(80)")
    private String contribuyente;

    @Column(columnDefinition = "VARCHAR(2)")
    private String tipo;

    private Long puntoVenta;

    private Long numeroFactura;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaEmision;

    @Column(precision = 20, scale = 2)
    private BigDecimal monto;

    private boolean activo;

    /* @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_asistencial")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "dni", "suplentes", "fechaNacimiento", "sexo",
            "telefono", "email", "domicilio", "estado", "activo", "autoridades", "registrosActividades", "descripcion",
            "esAsistencial", "registroMensual" })
    private Person asistencial; */

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_asistencial")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
            "estado", "tipoGuardia", "descripcion", "tiposGuardias", "registrosActividades", "dni",
            "fechaNacimiento",
            "sexo", "telefono", "email", "domicilio", "esAsistencial", "activo", "suplentes", "autoridades",
            "legajos",
            "cronogramasTentativos", "habilitacionesGuardias", "habilitacionesGenerales",
            "novedadesPersonales",
            "distribucionesHorarias", "registrosMensuales", "usuarios", "cronogramasDefinitivos", "facturas" })
    private Asistencial asistencial;

}
