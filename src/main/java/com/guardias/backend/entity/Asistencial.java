package com.guardias.backend.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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

}
