package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hospitales")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
public class Hospital extends Efector {

    private Boolean esCabecera;
    private Boolean admitePasiva;
    private Long nivelComplejidad;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cabecera", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "cabecera" })
    private List<Caps> caps = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "hospitales", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "hospitales" })
    private List<ValorGuardiaBase> valoresGuardiaBase = new ArrayList<>();

}
