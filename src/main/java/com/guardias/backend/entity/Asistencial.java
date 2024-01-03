package com.guardias.backend.entity;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "asistenciales")
@Data
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
@AllArgsConstructor
@NoArgsConstructor
public class Asistencial extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencial")
    private Long id;

    @Column(columnDefinition = "VARCHAR(30)")
    private String nombre;

    @Column(columnDefinition = "VARCHAR(30)")
    private String apellido;

    @Column(columnDefinition = "VARCHAR(30)")
    private String dni;

    @Column(columnDefinition = "VARCHAR(30)")
    private String cuil;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(columnDefinition = "DATE")
    private LocalDate fechaNacimiento;

    @Column(columnDefinition = "VARCHAR(20)")
    private String sexo;

    @Column(columnDefinition = "VARCHAR(30)")
    private String numCelular;

    @Column(columnDefinition = "VARCHAR(30)")
    private String email;

    @Column(columnDefinition = "VARCHAR(50)")
    private String domicilio;

    @Column(columnDefinition = "BOOLEAN")
    private Boolean estado;

    // Si necesitas legajos, puedes mantener esta relación
    @OneToMany(mappedBy = "asistencial")
    private Set<Legajo> legajos;

    @ManyToOne
    @JoinColumn(name = "id_Tipo_Guardia")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "asistencial" })
    private TipoGuardia tipoGuardia;

    @ManyToOne
    @JoinColumn(name = "id_especialidad")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "asistencial" })
    private Especialidad especialidad;
}