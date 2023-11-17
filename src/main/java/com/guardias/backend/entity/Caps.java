package com.guardias.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "caps")
@DiscriminatorValue("CAPS")
public class Caps extends Efector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_udo")
    private int idUdo;

    @Column(name = "tipo_caps")
    private String tipoCaps;

    public Caps() {
    }

    public Caps(Long id, int idUdo, String tipoCaps) {
        this.id = id;
        this.idUdo = idUdo;
        this.tipoCaps = tipoCaps;
    }

    public Caps(long idEfector, String nombre, String domicilio, String telefono, boolean estado, String observacion,
            long idRegion, long idLocalidad, Long id, int idUdo, String tipoCaps) {
        super(idEfector, nombre, domicilio, telefono, estado, observacion, idRegion, idLocalidad);
        this.id = id;
        this.idUdo = idUdo;
        this.tipoCaps = tipoCaps;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdUdo() {
        return idUdo;
    }

    public void setIdUdo(int idUdo) {
        this.idUdo = idUdo;
    }

    public String getTipoCaps() {
        return tipoCaps;
    }

    public void setTipoCaps(String tipoCaps) {
        this.tipoCaps = tipoCaps;
    }

}
