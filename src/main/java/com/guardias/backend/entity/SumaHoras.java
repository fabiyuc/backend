package com.guardias.backend.entity;

import java.math.BigDecimal;

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
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "sumaHoras")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SumaHoras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float horasLav;
    private float horasSdf;

    @Column(precision = 20, scale = 2)
    private BigDecimal montoLav;

    @Column(precision = 20, scale = 2)
    private BigDecimal montoSdf;
    
    @Column(precision = 20, scale = 2)
    private BigDecimal montoTotal;

    @OneToOne
    @JoinColumn(name = "registro_mensual_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "mes", "fechaEgreso", "anio",
            "registroActividad", "asistencial", "efector", "ddjj", "sumaHoras" })
    private RegistroMensual registroMensual;

    @OneToOne
    @JoinColumn(name = "registro_actividad_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "activo", "fechaIngreso", "fechaEgreso",
            "horaIngreso", "horaEgreso", "tipoGuardia", "asistencial", "servicio", "efector", "registroMensual",
            "registrosPendientes", "usuario", "horaRegistro", "fechaRegistro" })
    private RegistroActividad registroActividad;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_valor_guardia")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private ValorGuardiaBase valorGuardia;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SumaHoras other = (SumaHoras) obj;
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
