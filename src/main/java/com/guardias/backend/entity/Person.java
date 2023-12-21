package com.guardias.backend.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "person_sequence", sequenceName = "person_sequence", allocationSize = 1)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_sequence")
    private Long id;
    @Column(columnDefinition = "VARCHAR(50)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(30)")
    private String apellido;
    private int dni;
    @Column(columnDefinition = "VARCHAR(15)")
    private String cuil;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaNacimiento;
    @Column(columnDefinition = "VARCHAR(15)")
    private String sexo;
    @Column(columnDefinition = "VARCHAR(15)")
    private String telefono;
    @Column(columnDefinition = "VARCHAR(25)")
    private String email;
    @Column(columnDefinition = "VARCHAR(50)")
    private String domicilio;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean estado;

    @OneToMany(mappedBy = "persona")
    private Set<NovedadPersonal> personas;

    @OneToMany(mappedBy = "suplente")
    private Set<NovedadPersonal> suplentes;

}
