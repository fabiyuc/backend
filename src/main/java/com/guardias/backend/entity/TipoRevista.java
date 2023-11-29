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

@Entity(name = "tiposRevistas")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TipoRevista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    
    @OneToMany(mappedBy = "tipoRevista")
    private Set<Revista> revistas;


    public TipoRevista(String nombre) {
        this.nombre = nombre;
    }
}
