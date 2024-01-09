package com.guardias.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guardias.backend.enums.TipoDistribucion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "distribucionesHorarias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class DistribucionHoraria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(15)")
    @Enumerated(EnumType.STRING)
    private TipoDistribucion tipo; // para poder mapear
    private LocalDate fecha;
    private LocalTime horaIngreso;
    private BigDecimal cantidadHoras; // para calcular el dia y horario de salida

    @OneToOne
    @JoinColumn(name = "id_efector")
    private Efector efector; // para registrar el efector donde esta cumpliendo ESTA dist. horaria

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_distribucion_horaria")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "distribucionesHorarias" })
    private Person persona;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DistribucionHoraria other = (DistribucionHoraria) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (tipo != other.tipo)
            return false;
        if (fecha == null) {
            if (other.fecha != null)
                return false;
        } else if (!fecha.equals(other.fecha))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
        result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
        return result;
    }

}
