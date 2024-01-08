package com.guardias.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "distribucionesHorarias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class DistribucionHoraria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private LocalTime HoraIngreso;
    private BigDecimal cantidadHoras; // para calcular el dia y horario de salida

}
