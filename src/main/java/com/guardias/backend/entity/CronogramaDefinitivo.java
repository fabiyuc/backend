package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.QuincenaEnum;

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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "cronogramasDefinitivos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronogramaDefinitivo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private MesesEnum mes;

    private int anio;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_efector")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "autoridades", "domicilio", "telefono",
            "estado", "activo", "observacion", "region", "localidad", "distribucionesHorarias",
            "legajosUdo", "legajos", "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera",
            "areaProgramatica", "tipoCaps", "nivelComplejidad", "cabecera", "ministerios", "registrosActividades", "registroMensual", "ddjjs", "registrosPendientes", "servicios", "cronogramasDefinitivos", "habilitacionesGuardias", "habilitacionesGenerales", "cronogramasTentativos", "feriados" })
    private Efector efector;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cronogramaDefinitivo_ddjj", joinColumns = @JoinColumn(name = "id_cronogramaDefinitivo"), inverseJoinColumns = @JoinColumn(name = "id_ddjj"))
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "cronogramaDefinitivo", "activo", "mes", "anio", "subtotal", "total", "valorGmi", "efector", "director", "directorDPH", "estadoDdjjDirector", "estadoDdjjDirectorDPH", "enPosesionDirector", "enPosesionDirectorDPH", "motivoDirector", "motivoDirectorDPH", "observacionesDdjj", "cronogramasDefinitivos" })
    private List<Ddjj> ddjjs = new ArrayList<>();

   /*  @Column(columnDefinition = "VARCHAR(20)", nullable = true) //acepta valor null
    @Enumerated(EnumType.STRING)
    private QuincenaEnum quincena; */

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CronogramaDefinitivo other = (CronogramaDefinitivo) obj;
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
