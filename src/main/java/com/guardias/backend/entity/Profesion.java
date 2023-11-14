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

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Profesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Boolean esAsistencial;
    private String matriculaNacional;
    private String matriculaProvincial;

    @OneToMany(mappedBy = "legajo")
    private Set<Legajo> legajos;

    public Profesion(String nombre, Boolean esAsistencial, String matriculaNacional, String matriculaProvincial) {
        this.nombre = nombre;
        this.esAsistencial = esAsistencial;
        this.matriculaNacional = matriculaNacional;
        this.matriculaProvincial = matriculaProvincial;
    }
    
}
