package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.EstadoFacturacionEnum;
import com.guardias.backend.enums.MesesEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "registrosMensuales")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroMensual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private MesesEnum mes;

    private int anio;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_asistencial")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "dni", "suplentes", "fechaNacimiento", "sexo",
            "telefono", "email", "domicilio", "estado", "activo", "autoridades", "registrosActividades", "descripcion",
            "esAsistencial", "registroMensual", "distribucionesHorarias", "registrosMensuales", "usuarios",
            "habilitacionesGenerales" })
    private Person asistencial;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "registroMensual", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "asistencial", "efector",
            "registrosPendientes", "registroMensual", "usuarioIngreso", "usuarioEgreso",
            "fechaRegistroIngreso", "horaRegistroIngreso", "fechaRegistroEgreso", "horaRegistroEgreso" })
    private List<RegistroActividad> registroActividad = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_efector")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "autoridades", "domicilio", "telefono",
            "estado", "activo", "observacion", "region", "localidad", "distribucionesHorarias",
            "legajosUdo", "legajos", "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera",
            "areaProgramatica", "tipoCaps", "nivelComplejidad", "cabecera", "ministerios", "registrosActividades",
            "registroMensual", "ddjjs", "registrosPendientes", "servicios", "habilitacionesGuardias",
            "habilitacionesGenerales", "cronogramasTentativos", "feriados", "cronogramasDefinitivos" })
    private Efector efector;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "registrosMensuales", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "mes", "anio", "subtotal",
            "total", "estadoDdjj", "valorGmi", "registrosMensuales", "efector", "director", "directorDPH", "estadoDdjjDirector", "estadoDdjjDirectorDPH", "enPosesionDirector", "enPosesionDirectorDPH", "motivoDirector", "motivoDirectorDPH", "observacionesDdjj", "tipoGuardia", "cronogramasDefinitivos" })
    private List<Ddjj> ddjjs = new ArrayList<Ddjj>();
    
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "registrosMensuales", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "registrosMensuales", "asistencial", "nombreTitular", "apellidoTitular", "dniTitular", "cuilTitular", "contribuyente", "tipo", "puntoVenta", "numeroFactura" })
    private List<Factura> facturas = new ArrayList<Factura>();

    @OneToOne
    @JoinColumn(name = "total_horas_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "registroMensual" })
    private SumaHoras totalHoras;

    /* @Column(columnDefinition = "VARCHAR(20)", nullable = true) //acepta valor null
    @Enumerated(EnumType.STRING)
    private QuincenaEnum quincena; */

    @Column(columnDefinition = "VARCHAR(20)", nullable = true) //acepta valor null
    @Enumerated(EnumType.STRING)
    private EstadoFacturacionEnum estadoFacturacion;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RegistroMensual other = (RegistroMensual) obj;
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
