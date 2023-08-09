package com.guardias.backend.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "udo")
public class Udo {

       @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoUsuario;

    private String nombre;
    private String tipo;
    private Long nivel;
    private String domicilio;
    private String region;
    private String localidad;
    
}
