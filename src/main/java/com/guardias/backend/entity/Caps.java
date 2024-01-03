package com.guardias.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caps")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
public class Caps extends Efector {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cabecera")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "cabecera", "caps" })
    Hospital cabecera;

    @Column(columnDefinition = "int default 1")
    int areaProgramatica;

    @Column(name = "tipo_caps", columnDefinition = "VARCHAR(25)")
    private String tipoCaps;

}
