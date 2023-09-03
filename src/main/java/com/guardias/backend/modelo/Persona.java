package com.guardias.backend.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

//@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor

//@Table(name = "persona")
@MappedSuperclass
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersona;

    //@Column(name = "nombre")
    private String nombre;

    //@Column(name = "apellido")
    private String apellido;

    //@Column(name = "dni")
    private int dni;

    //@Column(name = "cuil")
    private String cuil;

    //@Column(name = "sexo")
    private String sexo;

    //@Column(name = "direccion")
    private String direccion;

    //@Column(name = "telefono")
    private String telefono;

    //@Column(name = "email")
    private String email;


   /*  @Column(name = "id_udo")
    private Long id_udo;

    @Column(name = "id_legajo")
    private Long id_legajo;

    @Column(name = "id_hospital")
    private Long id_hospital;

    @Column(name = "id_cargo")
    private Long id_cargo;

    @Column(name = "id_profesion")
    private Long id_profesion; */

  /*   @Column(name = "id_usuario")
    private Long id_usuario; */

   /*  @Column(name = "estado")
    private Long estado;
 */
    /* public Long getId_persona() {
        return id_persona;
    }

    public void setId_persona(Long id_persona) {
        this.id_persona = id_persona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId_udo() {
        return id_udo;
    }

    public void setId_udo(Long id_udo) {
        this.id_udo = id_udo;
    }

    public Long getId_legajo() {
        return id_legajo;
    }

    public void setId_legajo(Long id_legajo) {
        this.id_legajo = id_legajo;
    }

    public Long getId_hospital() {
        return id_hospital;
    }

    public void setId_hospital(Long id_hospital) {
        this.id_hospital = id_hospital;
    }

    public Long getId_cargo() {
        return id_cargo;
    }

    public void setId_cargo(Long id_cargo) {
        this.id_cargo = id_cargo;
    }

    public Long getId_profesion() {
        return id_profesion;
    }

    public void setId_profesion(Long id_profesion) {
        this.id_profesion = id_profesion;
    }

    /* public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    } */

    /* public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public Persona() {
    } */ 
/* 
    public Persona(Long id_persona, String nombre, String apellido, int dni, String cuil, String sexo, String direccion,
            String telefono, String email, Long id_udo, Long id_legajo, Long id_hospital, Long id_cargo,
            Long id_profesion, Long id_usuario, Long estado) {
        this.id_persona = id_persona;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.cuil = cuil;
        this.sexo = sexo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.id_udo = id_udo;
        this.id_legajo = id_legajo;
        this.id_hospital = id_hospital;
        this.id_cargo = id_cargo;
        this.id_profesion = id_profesion;
        this.id_usuario = id_usuario;
        this.estado = estado;
    } */

    /* public Persona(Long id_persona, String nombre, String apellido, int dni, String cuil, String sexo, String direccion,
            String telefono, String email, Long id_udo, Long id_legajo, Long id_hospital, Long id_cargo,
            Long id_profesion, Long estado) {
        this.id_persona = id_persona;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.cuil = cuil;
        this.sexo = sexo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.id_udo = id_udo;
        this.id_legajo = id_legajo;
        this.id_hospital = id_hospital;
        this.id_cargo = id_cargo;
        this.id_profesion = id_profesion;
        this.estado = estado;
    } */



    

}