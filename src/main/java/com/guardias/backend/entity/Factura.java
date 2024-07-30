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
    private String contribuyente;
    private LocalDate fecha;
    private String tipo;
    private String numero;
    @Column(precision = 20, scale = 2)
    private BigDecimal monto;
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_asistencial")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "dni", "suplentes", "fechaNacimiento", "sexo",
            "telefono",
            "email", "domicilio", "estado", "activo", "autoridades", "registrosActividades",
            "descripcion", "esAsistencial", "registroMensual" })
    private Person asistencial;

}
