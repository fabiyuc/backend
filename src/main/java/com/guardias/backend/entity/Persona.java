package com.guardias.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Table(name = "personas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(name = "nombre")
    @Column(columnDefinition = "VARCHAR(25)")
    private String nombre;

    // @Column(name = "apellido")
    @Column(columnDefinition = "VARCHAR(25)")
    private String apellido;

    // @Column(name = "dni")
    private int dni;

    // @Column(name = "cuil")
    @Column(columnDefinition = "VARCHAR(10)")
    private String cuil;

    // @Column(name = "sexo")
    @Column(columnDefinition = "VARCHAR(15)")
    private String sexo;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaNac;

    private Boolean estado;

    // @Column(name = "domicilio")
    @Column(columnDefinition = "VARCHAR(50)")
    private String domicilio;

    // @Column(name = "telefono")
    @Column(columnDefinition = "VARCHAR(15)")
    private String telefono;

    // @Column(name = "email")
    @Column(columnDefinition = "VARCHAR(20)")
    private String email;
}