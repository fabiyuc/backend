package com.guardias.backend.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "noAsistenciales")
@Data
@AllArgsConstructor
// @RequiredArgsConstructor
@NoArgsConstructor
public class NoAsistencial extends Person {

    private String descripcion;

    @OneToMany(mappedBy = "noAsistencial")
    private Set<Legajo> legajos;
}
