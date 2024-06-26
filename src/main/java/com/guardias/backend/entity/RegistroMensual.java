package com.guardias.backend.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import jakarta.persistence.OneToMany;
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
    private Long idAsistencial; // para que sea mas facil la busqueda por persona

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "registroMensual", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler",
            "activo", "fechaIngreso", "fechaEgreso", "horaIngreso", "horaEgreso", "tipoGuardia", "asistencial",
            "servicio", "efector", "registroMensual" })
    private List<RegistroActividad> registroActividad = new ArrayList<>();

    // @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo",
    // "mes", "fechaEgreso","anio",
    // "registroActividad","idAsistencial" })

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
