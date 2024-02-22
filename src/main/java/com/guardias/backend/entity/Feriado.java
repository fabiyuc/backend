package com.guardias.backend.entity;

import java.time.LocalDate;

import com.guardias.backend.enums.TipoFeriadoEnum;

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

@Entity(name = "feriados")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feriado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @Column(columnDefinition = "VARCHAR(25)")
    private String motivo;
    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private TipoFeriadoEnum tipoFeriado;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @Column(columnDefinition = "VARCHAR(50)")
    private String descripcion;

}
