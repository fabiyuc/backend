package com.guardias.backend.entity;

import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class NoAsistencial extends Person {
    
    private String descripcion;
    private String nombreUsuario;
    private String contraseña;
    private String tipo_usuario;

    @OneToMany(mappedBy = "noAsistencial")
    private Set<Legajo> legajos;
}
