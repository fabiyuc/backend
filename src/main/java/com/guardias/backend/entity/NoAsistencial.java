package com.guardias.backend.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "noAsistenciales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoAsistencial extends Person {

    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;

    @OneToMany(mappedBy = "noAsistencial")
    private Set<Legajo> legajos;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "novedadesNoAsistencial", cascade = CascadeType.ALL)
    private List<NovedadPersonal> novedadesNoAistencial;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reemplazantesNoAsistencial", cascade = CascadeType.ALL)
    private List<NovedadPersonal> reemplazantesNoAsistencial;
}
