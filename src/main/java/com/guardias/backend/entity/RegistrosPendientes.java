package com.guardias.backend.entity;

import java.time.LocalDate;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "registrosPendientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrosPendientes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate fecha;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_efector")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "autoridades", "domicilio", "telefono",
            "estado", "activo", "observacion", "region", "localidad", "distribucionesHorarias",
            "legajosUdo", "legajos", "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera",
            "areaProgramatica", "tipoCaps", "nivelComplejidad", "cabecera", "ministerios", "registrosActividades",
            "registroMensual", "ddjjs", "registrosPendientes" })
    private Efector efector;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "RegistrosPendientes", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
            "activo", "fechaIngreso", "fechaEgreso", "horaIngreso", "horaEgreso", "tipoGuardia", "asistencial",
            "servicio", "efector", "registrosPendientes" })
    private List<RegistroActividad> registrosActividades = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RegistrosPendientes other = (RegistrosPendientes) obj;
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
    // "fecha", "efector", "registrosActividades" })

}
