package com.guardias.backend.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(50)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(30)")
    private String apellido;
    private String dni;
    private String cuil;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaNacimiento;
    @Column(columnDefinition = "VARCHAR(15)")
    private String sexo;
    @Column(columnDefinition = "VARCHAR(15)")
    private String numCelular;
    @Column(columnDefinition = "VARCHAR(15)")
    private String email;
    @Column(columnDefinition = "VARCHAR(25)")
    private String domicilio;
    private Boolean estado;

    @OneToOne(mappedBy = "persona")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "persona" })
    private NovedadPersonal novedadPersonal;

    @OneToOne(mappedBy = "reemplazante")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "reemplazante" })
    private NovedadPersonal novedadPersonalReemplazante;

}
