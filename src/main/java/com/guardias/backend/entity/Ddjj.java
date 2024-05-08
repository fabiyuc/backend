package com.guardias.backend.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.EstadoDdjjEnum;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Ddjjs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ddjj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;
    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private MesesEnum mes;
    private int anio;
    @Column(precision = 20, scale = 2)
    private BigDecimal subtotal;
    @Column(precision = 20, scale = 2)
    private BigDecimal total;
    private EstadoDdjjEnum estadoDdjj;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_valorGmi")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "fechaInicio", "fechaFin", "monto", "tipoGuardia",
            "ddjjs", "activo" })
    private ValorGmi valorGmi;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_efector")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "autoridades", "domicilio", "telefono",
            "estado", "activo", "observacion", "region", "localidad", "distribucionesHorarias", "legajosUdo", "legajos",
            "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera", "areaProgramatica", "tipoCaps",
            "nivelComplejidad", "cabecera", "ministerios", "registrosActividades", "registroMensual", "ddjjs" })
    private Efector efector;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ddjj", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo",
            "mes", "fechaEgreso", "anio",
            "registroActividad", "idAsistencial", "efector", "ddjj", "sumaHoras" })
    List<RegistroMensual> registrosMensuales = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ddjj other = (Ddjj) obj;
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

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo",
    // "mes", "anio", "subtotal",
    // "total", "estadoDdjj","valorGmi" })

}