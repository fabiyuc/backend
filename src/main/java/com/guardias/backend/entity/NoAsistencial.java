package com.guardias.backend.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Noasistenciales")
@DiscriminatorValue("NO_ASISTENCIAL")
@PrimaryKeyJoinColumn(name = "id_persona")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoAsistencial extends Person {

    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;

    @OneToMany(mappedBy = "noAsistencial")
    private Set<Legajo> legajos;
}
