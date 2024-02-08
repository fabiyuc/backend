package com.guardias.backend.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.TipoGuardiaEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15)")
    private TipoGuardiaEnum tipoGuardia;

    @OneToMany(mappedBy = "asistencial")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "asistencial" })
    private Set<RegistroActividad> registrosActividades;

    // @ManyToMany
    // @JoinTable(name = "asistencial_especialidad", joinColumns = @JoinColumn(name
    // = "asistencial_id"), inverseJoinColumns = @JoinColumn(name =
    // "especialidad_id"))
    // private Set<Especialidad> especialidades;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "asistencial_especialidad", joinColumns = @JoinColumn(name = "asistencial_id"), inverseJoinColumns = @JoinColumn(name = "especialidad_id"))
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "asistenciales" })
    private Set<Especialidad> especialidades = new HashSet<>();

}