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
@Table(name = "ministerios")
@DiscriminatorValue("MINISTERIO")
@PrimaryKeyJoinColumn(name = "id_efector")
public class Caps extends Efector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_udo")
    private Long idUdo;

    @Column(name = "tipo_caps", columnDefinition = "VARCHAR(25)")
    private String tipoCaps;

    public Caps() {
    }

    public Caps(Long id, Long idUdo, String tipoCaps) {
        this.id = id;
        this.idUdo = idUdo;
        this.tipoCaps = tipoCaps;
    }

    public Caps(long idEfector, String nombre, String domicilio, String telefono, boolean estado, String observacion,
            long idRegion, long idLocalidad, Long id, Long idUdo, String tipoCaps) {
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

    public Long getIdUdo() {
        return idUdo;
    }

    public void setIdUdo(Long idUdo) {
        this.idUdo = idUdo;
    }

    public String getTipoCaps() {
        return tipoCaps;
    }

    public void setTipoCaps(String tipoCaps) {
        this.tipoCaps = tipoCaps;
    }

}
