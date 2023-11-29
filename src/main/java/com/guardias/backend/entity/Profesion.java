package com.guardias.backend.entity;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity(name = "profesiones")
@Data
@RequiredArgsConstructor
public class Profesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Boolean esAsistencial;

    @OneToMany(mappedBy = "profesion")
    private Set<Legajo> legajos;

    public Profesion(String nombre, Boolean esAsistencial) {
        this.nombre = nombre;
        this.esAsistencial = esAsistencial;
    }

}
