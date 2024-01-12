package com.guardias.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "distribucionHoraria_sequence", sequenceName = "distribucionHoraria_sequence", allocationSize = 1)
public abstract class DistribucionHoraria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "distribucionHoraria_sequence")
    private Long id;

    private LocalDate fecha;
    private LocalTime horaIngreso;
    private BigDecimal cantidadHoras; // para calcular el dia y horario de salida

    @OneToOne
    @JoinColumn(name = "id_efector")
    private Efector efector; // para registrar el efector donde esta cumpliendo ESTA dist. horaria

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_persona")
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
        if (fecha == null) {
            if (other.fecha != null)
                return false;
        } else if (!fecha.equals(other.fecha))
            return false;
        if (horaIngreso == null) {
            if (other.horaIngreso != null)
                return false;
        } else if (!horaIngreso.equals(other.horaIngreso))
            return false;
        if (efector == null) {
            if (other.efector != null)
                return false;
        } else if (!efector.equals(other.efector))
            return false;
        if (persona == null) {
            if (other.persona != null)
                return false;
        } else if (!persona.equals(other.persona))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
        result = prime * result + ((horaIngreso == null) ? 0 : horaIngreso.hashCode());
        result = prime * result + ((efector == null) ? 0 : efector.hashCode());
        result = prime * result + ((persona == null) ? 0 : persona.hashCode());
        return result;
    }

}
