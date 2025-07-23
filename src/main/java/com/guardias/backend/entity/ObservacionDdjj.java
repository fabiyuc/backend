package com.guardias.backend.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.security.entity.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "observacionesDdjj")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObservacionDdjj {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @Column(columnDefinition = "VARCHAR(300)")
    private String motivo;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean tipoDph;

    @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "usuario")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "nombre", "nombreUsuario", "email",
            "password", "registrosIngresos", "registrosEgresos", "person",
            "asistencial", "noAsistencial", "ddjjsDirector", "ddjjsDirectorDPH" , "observacionesDdjj", "roles"})
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ddjj")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "mes", "anio", "subtotal",
            "total", "estadoDdjj", "valorGmi", "registrosMensuales", "director", "directorDPH", "observacionesDdjj" })
    private Ddjj ddjj;

     @Temporal(TemporalType.DATE)
     private LocalDate fechaCreacion;

     @Temporal(TemporalType.TIME)
     private LocalTime horaCreacion;

}
