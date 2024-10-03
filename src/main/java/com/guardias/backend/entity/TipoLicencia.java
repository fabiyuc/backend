package com.guardias.backend.entity;

import java.time.LocalDate;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
    /*
     * @Column(columnDefinition = "VARCHAR(10)")
     * private String ley;
     * 
     * @Column(columnDefinition = "VARCHAR(10)")
     * private String articulo;
     * 
     * @Column(columnDefinition = "VARCHAR(10)")
     * private String inciso;
     */
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaFin;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoLicencia", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "estado",
            "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo",
            "novedadesPersonales",
            "tipoLey", "articuloPadre", "inciso", "incisos", "subIncisos", "subArticulos", "articulo",
            "incisoPadre", "tipoLicencia" })
    private List<Articulo> articulos = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoLicencia", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "estado",
            "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo",
            "novedadesPersonales",
            "tipoLey", "articuloPadre", "incisoPadre", "incisos", "subIncisos", "subArticulos",
            "tipoLicencia" })
    private List<Inciso> incisos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_tipo_ley")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "descripcion", "activo", "tipoLicencias" })
    private TipoLey tipoLey;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoLicencia", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "fechaInicio", "fechaFinal", "puedeRealizarGuardia",
            "cobraSueldo", "necesitaReemplazo", "actual", "descripcion", "persona", "suplente", "ley", "articuloPadre",
            "incisoPadre", "activo" })
    private List<NovedadPersonal> novedadesPersonales = new ArrayList<>();

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
