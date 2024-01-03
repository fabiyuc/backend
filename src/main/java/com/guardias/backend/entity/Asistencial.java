package com.guardias.backend.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "asistenciales")
@Data
@AllArgsConstructor
@NoArgsConstructor
// @EqualsAndHashCode(callSuper = false)
public class Asistencial extends Person {

    @ManyToOne
    @JoinColumn(name = "id_tipo_guardia")
    private TipoGuardia tipoGuardia;

    @OneToMany(mappedBy = "asistencial")
    private Set<Legajo> legajos;

    @ManyToMany
    @JoinTable(name = "asistencial_especialidad", joinColumns = @JoinColumn(name = "asistencial_id"), inverseJoinColumns = @JoinColumn(name = "especialidad_id"))
    private Set<Especialidad> especialidades = new HashSet<>();
}
