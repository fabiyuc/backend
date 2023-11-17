package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "ministerios")
@DiscriminatorValue("MINISTERIO")
public class Ministerio extends Efector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cabecera")
    private Long idCabecera;

    public Ministerio() {
    }

    public Ministerio(Long id, Long idCabecera) {
        this.id = id;
        this.idCabecera = idCabecera;
    }

    public Ministerio(long idEfector, String nombre, String domicilio, String telefono, boolean estado,
            String observacion, long idRegion, long idLocalidad, Long id, Long idCabecera) {
        super(idEfector, nombre, domicilio, telefono, estado, observacion, idRegion, idLocalidad);
        this.id = id;
        this.idCabecera = idCabecera;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCabecera() {
        return idCabecera;
    }

    public void setIdCabecera(Long idCabecera) {
        this.idCabecera = idCabecera;
    }

}
