package com.guardias.backend.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "cargos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(30)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;
    @Column(columnDefinition = "VARCHAR(10)")
    private String nroresolucion;
    @Column(columnDefinition = "VARCHAR(10)")
    private String nrodecreto;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(columnDefinition = "DATE")
    private LocalDate fecharesolucion;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(columnDefinition = "DATE")
    private LocalDate fechainicio;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(columnDefinition = "DATE")
    private LocalDate fechafinal;

}
