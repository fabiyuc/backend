package com.guardias.backend.modelo;


import java.sql.Date;
import java.sql.Time;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RegistroActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String establecimiento;
    private String servicio;
    private Date fechaIngreso;
    private Date fechaEgreso;
    private Time horaIngreso;
    private Time horaEgreso;
    
	@ManyToOne(optional = false)
    @JoinColumn(name = "id_tipo_guardia")
    private TipoGuardia tipoGuardia;
    
    public RegistroActividad(String establecimiento, String servicio, Date fechaIngreso, Date fechaEgreso,
            Time horaIngreso, Time horaEgreso, TipoGuardia tipoGuardia) {
        this.establecimiento = establecimiento;
        this.servicio = servicio;
        this.fechaIngreso = fechaIngreso;
        this.fechaEgreso = fechaEgreso;
        this.horaIngreso = horaIngreso;
        this.horaEgreso = horaEgreso;
        this.tipoGuardia = tipoGuardia;
    }


}
