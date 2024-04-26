package com.guardias.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "girasMedicas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiraMedica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidadHoras;
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GiraMedica other = (GiraMedica) obj;
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
    // "cantidadHoras", "fecha","activo","descripcion" })

}
