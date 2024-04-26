package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ministerios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@EqualsAndHashCode(callSuper = false) // no modifica los metodos Equals y Hash de la supereclase, pero si los utiliza
public class Ministerio extends Efector {

        @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "id_cabecera")
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre",
                        "autoridades", "domicilio", "telefono",
                        "estado", "activo",
                        "observacion", "region", "localidad", "distribucionesHorarias", "legajosUdo",
                        "legajos",
                        "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera",
                        "areaProgramatica",
                        "tipoCaps", "nivelComplejidad", "cabecera", "ministerios" })
        Ministerio cabecera;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "cabecera", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre",
                        "autoridades", "domicilio", "telefono",
                        "estado", "activo",
                        "observacion", "region", "localidad", "distribucionesHorarias", "legajosUdo",
                        "legajos",
                        "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera",
                        "areaProgramatica",
                        "tipoCaps", "nivelComplejidad", "cabecera", "ministerios" })
        List<Ministerio> ministerios = new ArrayList<>();

}
