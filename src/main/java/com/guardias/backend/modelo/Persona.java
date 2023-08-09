package com.guardias.backend.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "persona")
public class Persona {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersona;

    private String nombre;
    private String apellido;
    private Long dni;
    private String cuil;
    private String sexo;
    private String direccion;
    private String telefono;
    private String email;
    private Long idUdo;
    private Long idLegajo;
    private Long idUsuario;
    private Long idHospital;
    private Long idCargo;
    private Long idProfesion;



}
