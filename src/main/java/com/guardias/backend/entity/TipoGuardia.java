package com.guardias.backend.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tiposGuardias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoGuardia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(30)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(80)")
    private String descripcion;

    @OneToMany(mappedBy = "tipoGuardia")
    @JsonIgnore // Añade esta anotación para evitar la recursión infinita
    private Set<RegistroActividad> registroActividades;

    @OneToMany(mappedBy = "tipoGuardia", cascade = CascadeType.ALL)
    private Set<Asistencial> asistenciales;

}
