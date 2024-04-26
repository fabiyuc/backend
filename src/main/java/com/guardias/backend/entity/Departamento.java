package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "departamentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(50)")
    private String nombre;

    @Column(columnDefinition = "VARCHAR(10)")
    private String codigoPostal;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_provincia")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "gentilicio", "activo", "departamentos",
            "pais" })
    Provincia provincia;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "departamento", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "activo", "departamento", "efectores" })
    List<Localidad> localidades = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Departamento other = (Departamento) obj;
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

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
    // "nombre","codigoPostal","activo","departamentos", "gentilicio" })

}