package com.guardias.backend.modelo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//@Entity
//@Table(name = "usuario")
// , uniqueConstraints = @UniqueConstraint(columnNames = "usuario"))
public class Usuarios {

   // @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    //@Column(name = "usuario")
    private String usuario;

   // @Column(name = "contrasena")
    private String contrasena;

   // @Column(name = "estado")
    private boolean estado;

    /*
     * @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
     * 
     * @JoinTable(
     * name = "usuarios_tipoUsuario",
     * joinColumns = @JoinColumn(name = "usuario_id",referencedColumnName =
     * "idUsuario"),
     * inverseJoinColumns = @JoinColumn(name = "tipoUsuario_id",referencedColumnName
     * = "id_tipo_usuario")
     * )
     */

    /**
     * cambiar
     */
  //  @Column(name = "id_tipo_usuario")
    private Long id_tipo_usuario;

   // @Column(name = "fechaAlta")
    private LocalDate fechaAlta;

  //  @Column(name = "fechaUltimaSesion")
    private LocalDate fechaUltimaSesion;

  //  @Column(name = "intentosFallidos")
    private Long intentosFallidos;

    /*
     * @Column(name = "id_persona")
     * private Long id_persona;
     */

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

    /*
     * public Long getId_persona() {
     * return id_persona;
     * }
     */

    /*
     * public void setId_persona(Long id_persona) {
     * this.id_persona = id_persona;
     * }
     */

    public Usuarios() {
        super();
    }

    public Usuarios(Long idUsuario, String usuario, String contrasena, boolean estado, Long id_tipo_usuario,
            LocalDate fechaAlta, LocalDate fechaUltimaSesion, Long intentosFallidos) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
        this.id_tipo_usuario = id_tipo_usuario;
        this.fechaAlta = fechaAlta;
        this.fechaUltimaSesion = fechaUltimaSesion;
        this.intentosFallidos = intentosFallidos;
        // this.id_persona = id_persona;
    }

    public Usuarios(String usuario, String contrasena, boolean estado, LocalDate fechaAlta, LocalDate fechaUltimaSesion,
            Long intentosFallidos) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
        this.fechaAlta = fechaAlta;
        this.fechaUltimaSesion = fechaUltimaSesion;
        this.intentosFallidos = intentosFallidos;
    }

    @Override
    public String toString() {
        return "Usuario [idUsuario=" + idUsuario + ", usuario=" + usuario + ", contrasena=" + contrasena + ", estado="
                + estado + ", id_tipo_usuario=" + id_tipo_usuario + ", fechaAlta=" + fechaAlta + ", fechaUltimaSesion="
                + fechaUltimaSesion + ", intentosFallidos=" + intentosFallidos + "]";
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

}
