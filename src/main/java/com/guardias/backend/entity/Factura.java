package com.guardias.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.CondicionFiscalEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "facturas_registrosMensuales", joinColumns = @JoinColumn(name = "id_factura"), inverseJoinColumns = @JoinColumn(name = "id_registroMensual"))
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "facturas", "mes", "anio", "asistencial", "activo", "registroActividad", "efector", "ddjjs"})
    private List<RegistroMensual> registrosMensuales = new ArrayList<>();

    @Column(columnDefinition = "VARCHAR(50)")
    private String nombreTitular;

    @Column(columnDefinition = "VARCHAR(30)")
    private String apellidoTitular;

    private int dniTitular;

    @Column(columnDefinition = "VARCHAR(15)")
    private String cuilTitular;

    @Column(columnDefinition = "VARCHAR(30)")
    @Enumerated(EnumType.STRING)
    private CondicionFiscalEnum contribuyente;

    @Column(columnDefinition = "VARCHAR(2)")
    private String tipo;

    private Long puntoVenta;

    private Long numeroFactura;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaEmision;

    @Column(precision = 20, scale = 2)
    private BigDecimal monto;

    private boolean activo;

    

}
