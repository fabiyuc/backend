package com.guardias.backend.modelo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "usuario"))
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "estado")
    private boolean estado;

    @Column(name = "idTipoUsuario")
    private Long idTipoUsuario;

    @Column(name = "fechaAlta")
    private LocalDate fechaAlta;

    @Column(name = "fechaUltimaSesion")
    private LocalDate fechaUltimaSesion;

    @Column(name = "intentosFallidos")
    private Long intentosFallidos;

    @Column(name = "idPersona")
    private Long idPersona;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Long getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(Long idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaUltimaSesion() {
        return fechaUltimaSesion;
    }

    public void setFechaUltimaSesion(LocalDate fechaUltimaSesion) {
        this.fechaUltimaSesion = fechaUltimaSesion;
    }

    public Long getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(Long intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public Usuario(Long id, String usuario, String contrasena, boolean estado, Long idTipoUsuario, LocalDate fechaAlta,
            LocalDate fechaUltimaSesion, Long intentosFallidos, Long idPersona) {
        this.id = id;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
        this.idTipoUsuario = idTipoUsuario;
        this.fechaAlta = fechaAlta;
        this.fechaUltimaSesion = fechaUltimaSesion;
        this.intentosFallidos = intentosFallidos;
        this.idPersona = idPersona;
    }

    public Usuario(String usuario, String contrasena, boolean estado, Long idTipoUsuario, LocalDate fechaAlta,
            LocalDate fechaUltimaSesion, Long intentosFallidos, Long idPersona) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
        this.idTipoUsuario = idTipoUsuario;
        this.fechaAlta = fechaAlta;
        this.fechaUltimaSesion = fechaUltimaSesion;
        this.intentosFallidos = intentosFallidos;
        this.idPersona = idPersona;
    }

    public Usuario() {
    }

}
