package com.guardias.backend.entity;

import java.util.Objects;
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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "especialidades")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(50)")
    private String nombre;
    private boolean esPasiva;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_profesion")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "especialidades", "asistencial", "legajos" })
    private Profesion profesion;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Especialidad that = (Especialidad) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}