package com.guardias.backend.dto;

import java.time.LocalDate;


public class UsuarioRegistroDTO {

    private Long id_usuario;
    private String usuario;
    private String contrasena;
    private boolean estado;
    private Long id_tipo_usuario;
    private LocalDate fechaAlta;
    private LocalDate fechaUltimaSesion;
    private Long intentosFallidos;
   
    public Long getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
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
    public Long getId_tipo_usuario() {
        return id_tipo_usuario;
    }
    public void setId_tipo_usuario(Long id_tipo_usuario) {
        this.id_tipo_usuario = id_tipo_usuario;
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
    public UsuarioRegistroDTO(Long id_usuario, String usuario, String contrasena, boolean estado, Long id_tipo_usuario,
            LocalDate fechaAlta, LocalDate fechaUltimaSesion, Long intentosFallidos) {
        this.id_usuario = id_usuario;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
        this.id_tipo_usuario = id_tipo_usuario;
        this.fechaAlta = fechaAlta;
        this.fechaUltimaSesion = fechaUltimaSesion;
        this.intentosFallidos = intentosFallidos;
    }
    public UsuarioRegistroDTO(String usuario, String contrasena, boolean estado,
            LocalDate fechaAlta, LocalDate fechaUltimaSesion, Long intentosFallidos) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
       
        this.fechaAlta = fechaAlta;
        this.fechaUltimaSesion = fechaUltimaSesion;
        this.intentosFallidos = intentosFallidos;
    }
    public UsuarioRegistroDTO(String usuario) {
        this.usuario = usuario;
    }
    public UsuarioRegistroDTO() {
    }

}
