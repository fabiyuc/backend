package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "localidades")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Localidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(50)")
    private String nombre;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_departamento")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "codigoPostal", "activo", "departamentos",
            "gentilicio" })
    private Departamento departamento;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "localidad", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "autoridades", "domicilio", "telefono",
            "estado", "activo", "observacion", "region", "localidad", "distribucionesHorarias",
            "legajosUdo", "legajos", "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera",
            "areaProgramatica", "tipoCaps", "nivelComplejidad", "cabecera", "ministerios", "registrosActividades",
            "registroMensual", "ddjjs", "registrosPendientes" })
    private List<Efector> efectores = new ArrayList<>();

    // @JsonIgnoreProperties({ "hibernateLazyInitializer",
    // "handler","nombre","activo", "departamento", "efectores" })

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Localidad other = (Localidad) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
            return false;
        if (departamento == null) {
            if (other.departamento != null)
                return false;
        } else if (!departamento.equals(other.departamento))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((departamento == null) ? 0 : departamento.hashCode());
        return result;
    }
}
