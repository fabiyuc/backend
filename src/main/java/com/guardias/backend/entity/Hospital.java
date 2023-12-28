package com.guardias.backend.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    private boolean esCabecera;
    private int nivelComplejidad;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cabecera", cascade = CascadeType.ALL)
    List<Caps> caps;

}
