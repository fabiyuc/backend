package com.guardias.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.guardias.backend.enums.EstadoFechaLimiteEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.security.entity.Usuario;

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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Fecha límite para la presentación de DDJJ de un mes/año y tipo de guardia.
 * Cada creación o modificación genera un registro nuevo; el anterior queda como
 * histórico (REEMPLAZADA o ELIMINADA). Solo puede haber uno VIGENTE (activo)
 * por mes/año/tipo de guardia.
 */
@Entity(name = "fechas_limite_ddjj")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FechaLimiteDdjj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mes y año de las guardias
    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private MesesEnum mes;

    @Column(nullable = false)
    private int anio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tipo_guardia")
    private TipoGuardia tipoGuardia;

    // Fecha límite (debe caer en el mes siguiente al de las guardias)
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate fechaLimite;

    @Column(columnDefinition = "VARCHAR(300)")
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private EstadoFechaLimiteEnum estado;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private boolean activo;

    // Auditoría de creación
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario_creacion")
    private Usuario usuarioCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaHoraCreacion;

    // Auditoría de baja (reemplazo o eliminación)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_usuario_baja")
    private Usuario usuarioBaja;

    private LocalDateTime fechaHoraBaja;

    @Column(columnDefinition = "VARCHAR(300)")
    private String motivoBaja;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FechaLimiteDdjj other = (FechaLimiteDdjj) obj;
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
