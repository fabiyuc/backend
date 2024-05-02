package com.guardias.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.guardias.backend.enums.TipoGuardiaEnum;

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

@Entity(name = "ValoresGmi")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValorGmi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaFin;

    @Column(precision = 20, scale = 2)
    private BigDecimal monto;

    private TipoGuardiaEnum tipoGuardia;

    // @OneToMany(fetch = FetchType.LAZY, mappedBy = "ValorGmi", cascade =
    // CascadeType.ALL)
    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
    // "nombre","registroActividad" })
    // private List<Ddjj> ddjjs = new ArrayList<>();

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
    // "fechaInicio","fechaFin","monto","tipoGuardia","ddjjs","activo" })

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ValorGmi other = (ValorGmi) obj;
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
