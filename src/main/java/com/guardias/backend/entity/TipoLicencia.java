package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tiposLicencias")
@Data
@AllArgsConstructor
@NoArgsConstructor

/*
 * TODO debe ser reemplazado por las clases Articulo e Inciso
 */
public class TipoLicencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(30)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(10)")
    private String ley;
    @Column(columnDefinition = "VARCHAR(10)")
    private String articulo;
    @Column(columnDefinition = "VARCHAR(10)")
    private String inciso;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
    // "nombre","ley", "articulo", "inciso", "activo" })

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TipoLicencia other = (TipoLicencia) obj;
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
