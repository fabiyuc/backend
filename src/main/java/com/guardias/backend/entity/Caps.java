package com.guardias.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.TipoCaps;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caps")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
public class Caps extends Efector {

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_cabecera")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "autoridades", "domicilio", "telefono",
            "estado", "activo", "observacion", "region", "localidad", "distribucionesHorarias", "legajosUdo", "legajos",
            "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera", "areaProgramatica", "tipoCaps",
            "nivelComplejidad", "cabecera", "ministerios" })
    Hospital cabecera;

    @Column(columnDefinition = "int default 1")
    int areaProgramatica;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_caps", columnDefinition = "VARCHAR(25)")
    private TipoCaps tipoCaps;
}
