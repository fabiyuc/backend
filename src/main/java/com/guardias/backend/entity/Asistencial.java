package com.guardias.backend.entity;

import java.util.Set;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asistenciales")
@DiscriminatorValue("ASISTENCIAL")
@PrimaryKeyJoinColumn(name = "id_persona")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asistencial extends Persona {

    @OneToMany(mappedBy = "asistencial")
    private Set<Legajo> legajos;

}
