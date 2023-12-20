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
public abstract class Person {

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

    @OneToMany(mappedBy = "novedadesPersonales")
    private Set<NovedadPersonal> novedadesPersonales;

    @OneToMany(mappedBy = "suplente")
    private Set<NovedadPersonal> suplente;

}
