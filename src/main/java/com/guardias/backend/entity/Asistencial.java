package com.guardias.backend.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asistenciales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asistencial extends Person {

    @OneToMany(mappedBy = "asistencial")
    private Set<Legajo> legajos;

    @OneToMany(mappedBy = "novedadesAsistencial")
    private Set<NovedadPersonal> novedadesAsistencial;

    @OneToMany(mappedBy = "asistencialReemplazante")
    private Set<NovedadPersonal> asistencialReemplazante;

}
