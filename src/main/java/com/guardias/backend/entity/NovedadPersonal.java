package com.guardias.backend.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.TipoNovedadEnum;

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

@Entity(name = "novedadesPersonales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovedadPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicio;
    @Temporal(TemporalType.DATE)
    private LocalDate fechaFinal;
    private boolean puedeRealizarGuardia;
    private boolean cobraSueldo;
    private boolean necesitaReemplazo;
    
    @Column(nullable = false, columnDefinition = "BIT DEFAULT 1")
    private boolean actual; // Si la novedad es actual(1) o pasada(0)

    @Column(columnDefinition = "VARCHAR(80)")
    private TipoNovedadEnum descripcion;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_persona")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "dni", "cuil", "legajos",
            "novedadesPersonales", "suplentes",
            "distribucionesHorarias", "fechaNacimiento", "sexo", "telefono", "email",
            "domicilio",
            "estado", "activo", "autoridades", "tipoGuardia", "registrosActividades",
            "descripcion", "esAsistencial" })
    private Person persona;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_suplente")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "dni", "cuil", "legajos",
            "novedadesPersonales", "suplentes",
            "distribucionesHorarias", "fechaNacimiento", "sexo", "telefono", "email",
            "domicilio",
            "estado", "activo", "autoridades", "tipoGuardia", "registrosActividades",
            "descripcion", "esAsistencial" })
    private Person suplente;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_articulo")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "numero", "denominacion", "detalle", "estado",
            "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo", "novedadesPersonales",
            "tipoLey", "articulo", "inciso", "incisos", "subIncisos", "subArticulos" })
    private Articulo articulo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_inciso")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "numero", "denominacion", "detalle", "estado",
            "fechaAlta", "fechaBaja", "fechaModificacion", "motivoModificacion", "activo", "novedadesPersonales",
            "tipoLey", "articulo", "inciso", "incisos", "subIncisos", "subArticulos" })
    private Inciso inciso;

    // @JsonIgnoreProperties({ "hibernateLazyInitializer",
    // "handler","fechaInicio","fechaFinal","puedeRealizarGuardia","cobraSueldo","necesitaReemplazo","actual","descripcion","persona","suplente","ley","articulo","inciso",
    // "activo" })

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NovedadPersonal other = (NovedadPersonal) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

}
