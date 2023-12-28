package com.guardias.backend.entity;

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
@Table(name = "ministerios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
public class Ministerio extends Efector {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cabecera")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "ministerios" })
    Ministerio cabecera;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cabecera", cascade = CascadeType.ALL)
    List<Ministerio> ministerios;

}
