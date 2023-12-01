package com.guardias.backend.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity(name = "asistenciales")
@Data
@AllArgsConstructor
public class Asistencial extends Person {

    @OneToMany(mappedBy = "asistencial")
    private Set<Legajo> legajos;

}
