package com.guardias.backend.dto;

import java.sql.Date;
import jakarta.validation.constraints.NotBlank;

public class NoAsistencialDto {
    
    @NotBlank
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private int dni;
    
    @NotBlank
    private String cuil;

    @NotBlank
    private Date fechaNacimiento;

    @NotBlank
    private String sexo;

    @NotBlank
    private String numCelular;

    @NotBlank
    private String email;

    @NotBlank
    private String domicilio;

    @NotBlank
    private boolean estado;

    public NoAsistencialDto() {
    }
    
    public NoAsistencialDto(@NotBlank String nombre, @NotBlank String apellido, @NotBlank int dni,
            @NotBlank String cuil, @NotBlank Date fechaNacimiento, @NotBlank String sexo, @NotBlank String numCelular,
            @NotBlank String email, @NotBlank String domicilio, @NotBlank boolean estado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.cuil = cuil;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.numCelular = numCelular;
        this.email = email;
        this.domicilio = domicilio;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNumCelular() {
        return numCelular;
    }

    public void setNumCelular(String numCelular) {
        this.numCelular = numCelular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
