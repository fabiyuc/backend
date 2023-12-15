package com.guardias.backend.entity;

import java.sql.Date;
import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor

@MappedSuperclass
public class Person {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String cuil;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String numCelular;
    private String email;
    private String domicilio;
    private Boolean estado;
    
    
}
