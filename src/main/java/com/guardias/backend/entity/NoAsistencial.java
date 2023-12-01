package com.guardias.backend.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "noAsistenciales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoAsistencial extends Person { // !! extiende de PERSON o de PERSONA??

    private String descripcion;
    private String nombreUsuario;
    private String contraseña;
    private String tipo_usuario;

    @OneToMany(mappedBy = "noAsistencial")
    private Set<Legajo> legajos;
}
