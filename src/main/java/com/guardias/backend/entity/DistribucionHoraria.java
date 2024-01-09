package com.guardias.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @OneToOne
    @JoinColumn(name = "id_efector")
    private Efector efector; // para registrar el efector donde esta cumpliendo ESTA dist. horaria

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_distribucion_horaria")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "distribucionesHorarias" })
    private Person persona;

}
