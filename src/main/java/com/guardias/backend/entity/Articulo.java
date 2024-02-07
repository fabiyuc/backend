package com.guardias.backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_articulo")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "subArticulos", "articulo" })
    private Articulo articulo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "articulo", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "subArticulos" })
    private List<Articulo> subArticulos;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "articulo", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "incisos" })
    private List<Inciso> incisos;

    @OneToOne
    @JoinColumn(name = "id_novedad_personal")
    private NovedadPersonal novedadPersonal;

}
