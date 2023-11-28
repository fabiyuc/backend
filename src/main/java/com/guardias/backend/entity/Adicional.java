package com.guardias.backend.entity;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor

public class Adicional {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
/* 
    @OneToMany(mappedBy = "adicional")
    private Set<Revista> revistas; */

    public Adicional(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}
