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
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "profesiones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(25)")
    private String nombre;
    private Boolean asistencial;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profesion", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "esPasiva", "activo", "profesion" })
    private List<Especialidad> especialidades = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profesion", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "fechaInicio", "fechaFinal", "actual", "legal",
            "activo", "matriculaNacional", "matriculaProvincial", "profesion", "suspencion", "revista", "udo",
            "persona", "cargo", "efectores" })
    private List<Legajo> legajos = new ArrayList<>();

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre",
    // "activo", "asistencial", "especialidades", "legajos"})

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Profesion other = (Profesion) obj;
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
