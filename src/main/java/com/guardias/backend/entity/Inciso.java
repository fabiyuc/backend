package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "incisos")
@Data
@AllArgsConstructor
@NoArgsConstructor
// @EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y
// Hash de la supereclase, pero si los utiliza
public class Inciso extends Ley {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_inciso_padre")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "numero", "denominacion", "detalle", "estado",
                        "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo",
                        "novedadesPersonales", "tipoLicencia",
                        "tipoLey", "articuloPadre", "incisoPadre", "incisos", "subIncisos", "subArticulos" })
        private Inciso incisoPadre;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "incisoPadre", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "estado",
                        "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo",
                        "novedadesPersonales", "tipoLicencia",
                        "tipoLey", "articuloPadre", "incisoPadre", "incisos", "subIncisos", "subArticulos" })
        private List<Inciso> subIncisos = new ArrayList<>();

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_articulo")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "estado",
                        "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo",
                        "novedadesPersonales", "tipoLicencia",
                        "tipoLey", "articuloPadre", "incisoPadre", "incisos", "subIncisos", "subArticulos" })
        private Articulo articulo;

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "descripcion", "activo", "tipoLicencias",
                        "articulo", "incisos", "fechaInicio", "fechaFin", "tipoLey", "novedadesPersonales",
                        "articulos" })
        @JoinColumn(name = "id_tipo_licencia")
        private TipoLicencia tipoLicencia;

        @Override
        public boolean equals(Object obj) {
                if (this == obj) {
                        return true;
                }
                if (obj == null) {
                        return false;
                }
                if (getClass() != obj.getClass()) {
                        return false;
                }
                Inciso other = (Inciso) obj;
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