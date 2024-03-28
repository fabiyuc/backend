package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "cargasHorarias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CargaHoraria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;
    @Column(columnDefinition = "VARCHAR(50)")
    private String descripcion; // si lleva algun adicional o algo a tener en cuenta
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @OneToMany(mappedBy = "cargaHoraria", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "tipoRevista", "categoria", "adicional", "cargaHoraria",
            "legajos", "agrupacion", "activo" })
    private List<Revista> revistas = new ArrayList<>();

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler","cantidad",
    // "descripcion","activo","revistas" })
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CargaHoraria other = (CargaHoraria) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

}
