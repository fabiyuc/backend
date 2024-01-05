package com.guardias.backend.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "asistenciales")
@Entity(name = "asistenciales")
@Data
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
@AllArgsConstructor
@NoArgsConstructor
// @EqualsAndHashCode(callSuper = false)
public class Asistencial extends Person {

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