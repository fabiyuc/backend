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
@Table(name = "articulos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
public class Articulo extends Ley {

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_articulo")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "numero", "denominacion", "detalle", "estado",
                        "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo",
                        "novedadesPersonales",
                        "tipoLey", "articulo", "inciso", "incisos", "subIncisos", "subArticulos" })
        private Articulo articulo;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "articulo", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "numero", "denominacion", "detalle", "estado",
                        "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo",
                        "novedadesPersonales",
                        "tipoLey", "articulo", "inciso", "incisos", "subIncisos", "subArticulos" })
        private List<Articulo> subArticulos = new ArrayList<>();

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "articulo", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "numero", "denominacion", "detalle", "estado",
                        "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo",
                        "novedadesPersonales",
                        "tipoLey", "articulo", "inciso", "incisos", "subIncisos", "subArticulos" })
        private List<Inciso> incisos = new ArrayList<>();
}
