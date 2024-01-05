package com.guardias.backend.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity(name = "registrosActividades")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(50)")
    private String establecimiento; // VER NO DEBERIA SER EL ID DEL EFECTOR??????????????????
    @Column(columnDefinition = "VARCHAR(50)")
    private String servicio; // VER CONVENDRIA HACER UNA TABLA SERVICIOS????????????
    @Temporal(TemporalType.DATE)
    private LocalDate fechaIngreso; // ! Date ya no se usa....
    @Temporal(TemporalType.DATE)
    private LocalDate fechaEgreso;
    @Temporal(TemporalType.TIME)
    private LocalTime horaIngreso;
    @Temporal(TemporalType.TIME)
    private LocalTime horaEgreso;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_tipo_guardia")
    private TipoGuardia tipoGuardia;

}
