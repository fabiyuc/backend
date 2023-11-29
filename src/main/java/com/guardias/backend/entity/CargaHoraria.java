package com.guardias.backend.entity;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity(name = "CargasHorarias")
@Data
public class CargaHoraria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int cantidad;
    
    @OneToMany(mappedBy = "cargaHoraria")
    private Set<Revista> revistas;

    public CargaHoraria(int cantidad) {
        this.cantidad = cantidad;
    }

}
