package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "efector_sequence", sequenceName = "efector_sequence", allocationSize = 1)
public abstract class Efector {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "efector_sequence")
    private Long id;
    @Column(columnDefinition = "VARCHAR(50)")
    private String nombre;
    @Column(columnDefinition = "VARCHAR(80)")
    private String domicilio;
    @Column(columnDefinition = "VARCHAR(15)")
    private String telefono;
    private boolean estado;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    private String observacion;

    private float porcentajePorZona;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_region")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "efectores" })
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_localidad")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "departamento", "efectores" })
    private Localidad localidad;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "efector", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "dia", "fechaInicio", "fechaFinalizacion",
    "horaIngreso", "cantidadHoras", "activo", "efector", "persona", "lugar", "especialidad", "cantidadTurnos",
    "destino", "descripcion", "tipoGuardia"})
    private List<DistribucionHoraria> distribucionesHorarias = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "udo", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "fechaInicio", "fechaFinal", "actual", "legal",
            "activo", "matriculaNacional", "matriculaProvincial", "especialidades", "suspencion", "revista", "udo",
            "persona", "cargo", "efectores" })
    private List<Legajo> legajosUdo = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "efectores", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "fechaInicio", "fechaFinal", "actual", "legal",
            "activo", "matriculaNacional", "matriculaProvincial", "profesion", "suspencion", "revista", "udo",
            "persona", "cargo", "efectores" })
    private List<Legajo> legajos = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "efectores", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",  "registrosActividades" , "efectores"})
    private List<Servicio> servicios = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "efectores", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notificacion> notificaciones = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "efector", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "fechaInicio", "fechaFinal", "esActual",
            "esRegional", "activo", "efector", "persona" })
    private List<Autoridad> autoridades = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "efector", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
            "activo", "fechaIngreso", "fechaEgreso", "horaIngreso", "horaEgreso", "tipoGuardia", "asistencial",
            "servicio", "efector", "registrosPendientes" })
    private List<RegistroActividad> registrosActividades = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "efector", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "mes", "fechaEgreso", "anio",
            "registroActividad", "idAsistencial", "efector", "ddjj", "sumaHoras" })
    private List<RegistroMensual> registroMensual = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "efector", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "mes", "anio", "subtotal",
            "total", "estadoDdjj", "valorGmi" })
    private List<Ddjj> ddjjs = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "efector", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
            "activo", "fecha", "efector", "registrosActividades" })
    private List<RegistrosPendientes> registrosPendientes = new ArrayList<>();

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre",
    // "autoridades", "domicilio", "telefono",
    // "estado", "activo", "observacion", "region", "localidad",
    // "distribucionesHorarias", "legajosUdo", "legajos",
    // "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera",
    // "areaProgramatica", "tipoCaps",
    // "nivelComplejidad", "cabecera", "ministerios", "registrosActividades",
    // "registroMensual", "ddjjs","registrosPendientes" })

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Efector other = (Efector) obj;
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
        if (domicilio == null) {
            if (other.domicilio != null)
                return false;
        } else if (!domicilio.equals(other.domicilio))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((domicilio == null) ? 0 : domicilio.hashCode());
        return result;
    }

}
