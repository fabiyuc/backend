package com.guardias.backend.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.TipoNotificacionEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "notificaciones")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private TipoNotificacionEnum tipo;

    @Column(columnDefinition = "VARCHAR(50)")
    private String categoria;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaNotificacion;

    @Column(columnDefinition = "VARCHAR(80)")
    private String detalle;

    @Column(columnDefinition = "VARCHAR(50)")
    private String url;
    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @Temporal(TemporalType.DATE)
    private LocalDate fechaBaja;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "notificacion_efector", joinColumns = @JoinColumn(name = "notificacion_id"), inverseJoinColumns = @JoinColumn(name = "efector_id"))
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "nombre", "autoridades", "domicilio", "telefono",
            "estado", "activo", "observacion", "region", "localidad", "distribucionesHorarias", "legajosUdo", "legajos",
            "notificaciones", "esCabecera", "admitePasiva", "caps", "cabecera", "areaProgramatica", "tipoCaps",
            "nivelComplejidad", "cabecera", "ministerios", "registroActividad", "ddjjs" })
    private List<Efector> efectores = new ArrayList<>();

    // @JsonIgnoreProperties({ "hibernateLazyInitializer",
    // "handler","tipo","activo", "categoria", "fechaNotificacion", "detalle",
    // "url", "fechaBaja","efectores" })

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Notificacion other = (Notificacion) obj;
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
