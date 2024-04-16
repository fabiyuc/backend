package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "incisos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
public class Inciso extends Ley {

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_inciso_padre")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "numero", "denominacion", "detalle", "estado",
                        "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo",
                        "novedadesPersonales",
                        "tipoLey", "articuloPadre", "incisoPadre", "incisos", "subIncisos", "subArticulos" })
        private Inciso incisoPadre;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "incisoPadre", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "numero", "denominacion", "detalle", "estado",
                        "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo",
                        "novedadesPersonales",
                        "tipoLey", "articuloPadre", "incisoPadre", "incisos", "subIncisos", "subArticulos" })
        private List<Inciso> subIncisos = new ArrayList<>();

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_articulo")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "numero", "denominacion", "detalle", "estado",
                        "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo",
                        "novedadesPersonales",
                        "tipoLey", "articuloPadre", "incisoPadre", "incisos", "subIncisos", "subArticulos" })
        private Articulo articulo;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "inciso", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({ "hibernateLazyInitializer",
                        "handler", "fechaInicio", "fechaFinal", "puedeRealizarGuardia", "cobraSueldo",
                        "necesitaReemplazo", "actual", "descripcion", "persona", "suplente", "ley", "articuloPadre",
                        "incisoPadre",
                        "activo" })
        private List<NovedadPersonal> novedadesPersonales = new ArrayList<>();
}
