package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "hospitales")
@DiscriminatorValue("HOSPITAL")
@PrimaryKeyJoinColumn(name = "id_efector")
public class Hospital extends Efector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean esCabecera;

    @Column(name = "nivel_complejidad")
    private int nivelComplejidad;

    public Hospital() {
    }

    public Hospital(Long id, boolean esCabecera, int nivelComplejidad) {
        this.id = id;
        this.esCabecera = esCabecera;
        this.nivelComplejidad = nivelComplejidad;
    }

    public Hospital(long idEfector, String nombre, String domicilio, String telefono, boolean estado,
            String observacion, long idRegion, long idLocalidad, Long id, boolean esCabecera, int nivelComplejidad) {
        super(idEfector, nombre, domicilio, telefono, estado, observacion, idRegion, idLocalidad);
        this.id = id;
        this.esCabecera = esCabecera;
        this.nivelComplejidad = nivelComplejidad;
    }

    public boolean isEsCabecera() {
        return esCabecera;
    }

    public void setEsCabecera(boolean esCabecera) {
        this.esCabecera = esCabecera;
    }

    public int getNivelComplejidad() {
        return nivelComplejidad;
    }

    public void setNivelComplejidad(int nivelComplejidad) {
        this.nivelComplejidad = nivelComplejidad;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEfector() {
        return id;
    }

}
